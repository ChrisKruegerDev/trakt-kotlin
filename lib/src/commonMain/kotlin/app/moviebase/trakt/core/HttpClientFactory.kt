package app.moviebase.trakt.core

import app.moviebase.trakt.TraktClientConfig
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
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

            // see https://ktor.io/docs/auth.html
            val authCredentials = config.traktAuthCredentials
            if (authCredentials != null) {
                install(Auth) {
                    bearer {
                        loadTokens {
                            authCredentials.loadTokensProvider()
                        }

                        refreshTokens {
                            authCredentials.refreshTokensProvider()
                        }

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
                    if (response.status.value !in 200..299) {
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
            }

            config.maxRequestRetries?.takeIf { it > 0 }?.let {
                install(HttpRequestRetry) {
                    retryOnExceptionIf(maxRetries = it) { _, cause ->
                        when {
                            cause is TraktException -> false
                            cause is kotlin.coroutines.cancellation.CancellationException -> false
                            else -> cause.isRetryableException()
                        }
                    }

                    exponentialDelay(maxDelayMs = 30_000)
                }
            }

            // see https://ktor.io/docs/client-caching.html
            if (config.useCache) {
                install(HttpCache)
            }

            if (config.useTimeout) {
                install(HttpTimeout) {
                    requestTimeoutMillis = 60_000
                    connectTimeoutMillis = 60_000
                    socketTimeoutMillis = 60_000
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
