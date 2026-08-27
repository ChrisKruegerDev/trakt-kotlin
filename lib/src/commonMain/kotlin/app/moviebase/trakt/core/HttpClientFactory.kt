package app.moviebase.trakt.core

import app.moviebase.trakt.TraktClientConfig
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

internal object HttpClientFactory {
    fun create(config: TraktClientConfig): HttpClient {
        val defaultConfig: HttpClientConfig<*>.() -> Unit = {
            val json = JsonFactory.create()

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = TraktWebConfig.HOST
                }
                contentType(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                json(json)
            }

            // see https://ktor.io/docs/client-content-encoding.html
            install(ContentEncoding) {
                gzip()
                deflate()
            }

            // see https://ktor.io/docs/auth.html
            val authCredentials = config.traktAuthCredentials
            if (authCredentials != null) {
                install(Auth) {
                    bearer {
                        loadTokens {
                            authCredentials.loadTokensProvider()
                        }

                        refreshTokens {
                            authCredentials.refreshTokensProvider(this)
                        }

                        nonCancellableRefresh = authCredentials.nonCancellableRefresh

                        sendWithoutRequest { request ->
                            request.url.host == TraktWebConfig.HOST
                        }
                    }
                }
            }

            // Disable Ktor's built-in validation so all errors go through TraktException
            expectSuccess = false

            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status.isSuccess()) return@validateResponse

                    val bodyText = response.bodyAsText()
                    val errorBody = parseErrorBody(json, bodyText)
                    throw TraktException(
                        statusCode = response.status.value,
                        requestUrl = response.call.request.url.toString(),
                        body = bodyText,
                        errorBody = errorBody,
                        headers = response.headers.entries().associate { it.key to it.value.joinToString(",") },
                    )
                }
            }

            // see https://ktor.io/docs/client-retry.html
            config.maxRequestRetries?.takeIf { it > 0 }?.let { maxRetries ->
                install(HttpRequestRetry) {
                    retryIf(maxRetries) { _, response ->
                        response.status.value in 500..599 ||
                            response.status == HttpStatusCode.TooManyRequests
                    }

                    retryOnExceptionIf(maxRetries) { _, cause ->
                        cause !is CancellationException && cause.isRetryableException()
                    }

                    exponentialDelay(
                        maxDelayMs = 30_000,
                        respectRetryAfterHeader = true,
                    )
                }
            }

            // see https://ktor.io/docs/client-caching.html
            if (config.useCache) {
                install(HttpCache)
            }

            if (config.useTimeout) {
                install(HttpTimeout) {
                    connectTimeoutMillis = 10_000   // host reachability — fail fast
                    socketTimeoutMillis = 30_000   // stall detection mid-response
                    requestTimeoutMillis = 30_000   // total ceiling per attempt
                }
            }

            config.httpClientLoggingBlock?.let {
                Logging(it)
            }

            // add custom configuration
            config.httpClientConfigBlock?.invoke(this)
        }

        return config.httpClientBuilder?.invoke()?.config(defaultConfig) ?: HttpClient(defaultConfig)
    }

    private fun Throwable.isRetryableException(): Boolean {
        val exception = unwrapCancellationException()
        return exception is HttpRequestTimeoutException ||
            exception is ConnectTimeoutException ||
            exception is SocketTimeoutException ||
            exception is IOException
    }
}
