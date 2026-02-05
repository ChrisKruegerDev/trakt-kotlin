package app.moviebase.trakt

import app.moviebase.trakt.core.TraktDsl
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.logging.LoggingConfig

@TraktDsl
class TraktClientConfig {

    var clientId: String? = null
    var clientSecret: String? = null

    internal var traktAuthCredentials: TraktAuthCredentials? = null

    var expectSuccess: Boolean = true
    var useCache: Boolean = true
    var useTimeout: Boolean = true
    var maxRequestRetries: Int? = 5

    internal var httpClientConfigBlock: (HttpClientConfig<*>.() -> Unit)? = null
    internal var httpClientBuilder: (() -> HttpClient)? = null
    internal var httpClientLoggingBlock: (LoggingConfig.() -> Unit)? = null

    fun userAuthentication(block: TraktAuthCredentials.() -> Unit) {
        traktAuthCredentials = TraktAuthCredentials().apply(block)
    }

    fun logging(block: LoggingConfig.() -> Unit) {
        httpClientLoggingBlock = block
    }

    /**
     * Set custom HttpClient configuration for the default HttpClient.
     */
    fun httpClient(block: HttpClientConfig<*>.() -> Unit) {
        this.httpClientConfigBlock = block
    }

    /**
     * Creates an custom [HttpClient] with the specified [HttpClientEngineFactory] and optional [block] configuration.
     * Note that the Trakt config will be added afterwards.
     */
    fun <T : HttpClientEngineConfig> httpClient(
        engineFactory: HttpClientEngineFactory<T>,
        block: HttpClientConfig<T>.() -> Unit = {},
    ) {
        httpClientBuilder = {
            HttpClient(engineFactory, block)
        }
    }

    companion object {
        internal fun withKey(tmdbApiKey: String) =
            TraktClientConfig().apply {
                this.clientId = tmdbApiKey
            }
    }
}

@TraktDsl
class TraktAuthCredentials {
    internal var refreshTokensProvider: suspend () -> BearerTokens? = { null }
    internal var loadTokensProvider: suspend () -> BearerTokens? = { null }

    fun refreshTokens(provider: suspend () -> BearerTokens?) {
        refreshTokensProvider = provider
    }

    fun loadTokens(provider: suspend () -> BearerTokens?) {
        loadTokensProvider = provider
    }
}
