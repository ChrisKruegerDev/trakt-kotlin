package app.moviebase.trakt

import app.moviebase.trakt.core.JsonFactory
import app.moviebase.trakt.core.TraktException
import app.moviebase.trakt.model.TraktShow
import com.google.common.truth.Truth.assertThat
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TraktRateLimitTest {
    val result = TraktShow(title = "Vikings")
    val show = JsonFactory.create().encodeToString(result)

    @Test
    fun `it retries rate limited requests and eventually succeeds`() =
        runTest {
            var requests = 0
            val config =
                mockHttpErrorConfig(
                    result = show,
                    status = HttpStatusCode.TooManyRequests,
                    timesToFail = 2,
                    maxRetries = 3,
                    retryAfterSeconds = 1,
                    onRequest = { requests++ },
                )
            val client = Trakt(config)

            val summary = client.shows.getSummary("vikings", TraktExtended.FULL)

            assertThat(summary.title).isEqualTo("Vikings")
            assertThat(requests).isEqualTo(3) // 2 rate-limited attempts + 1 success
        }

    @Test
    fun `it retries transient server errors and eventually succeeds`() =
        runTest {
            var requests = 0
            val config =
                mockHttpErrorConfig(
                    result = show,
                    status = HttpStatusCode.ServiceUnavailable,
                    timesToFail = 2,
                    maxRetries = 3,
                    onRequest = { requests++ },
                )
            val client = Trakt(config)

            val summary = client.shows.getSummary("vikings", TraktExtended.FULL)

            assertThat(summary.title).isEqualTo("Vikings")
            assertThat(requests).isEqualTo(3) // 2 server errors + 1 success
        }

    @Test
    fun `it gives up after the max retries and preserves the retry after header`() =
        runTest {
            var requests = 0
            val config =
                mockHttpErrorConfig(
                    result = show,
                    status = HttpStatusCode.TooManyRequests,
                    timesToFail = Int.MAX_VALUE,
                    maxRetries = 2,
                    retryAfterSeconds = 1,
                    onRequest = { requests++ },
                )
            val client = Trakt(config)

            val exception =
                assertThrows<TraktException> {
                    client.shows.getSummary("vikings", TraktExtended.FULL)
                }

            assertThat(exception.statusCode).isEqualTo(429)
            assertThat(exception.retryAfterSeconds).isEqualTo(1)
            assertThat(exception.header("retry-after")).isEqualTo("1")
            assertThat(requests).isEqualTo(3) // initial attempt + 2 retries
        }

    @Test
    fun `it does not retry permanent client errors`() =
        runTest {
            var requests = 0
            val config =
                mockHttpErrorConfig(
                    result = show,
                    status = HttpStatusCode.NotFound,
                    timesToFail = Int.MAX_VALUE,
                    maxRetries = 3,
                    onRequest = { requests++ },
                )
            val client = Trakt(config)

            val exception =
                assertThrows<TraktException> {
                    client.shows.getSummary("vikings", TraktExtended.FULL)
                }

            assertThat(exception.statusCode).isEqualTo(404)
            assertThat(requests).isEqualTo(1) // no retries for a 404
        }

    private fun mockHttpErrorConfig(
        result: String,
        status: HttpStatusCode,
        timesToFail: Int = 1,
        maxRetries: Int = 3,
        retryAfterSeconds: Int? = null,
        onRequest: () -> Unit = {},
    ): TraktClientConfig.() -> Unit =
        {
            clientId = "someKey"
            useCache = true
            useTimeout = true

            maxRequestRetries = maxRetries

            var times = timesToFail

            httpClient(MockEngine) {
                logging {
                    logger = TestLogger()
                    level = LogLevel.HEADERS
                }

                engine {
                    addHandler { _ ->
                        onRequest()
                        if (times > 0) {
                            times--

                            val errorHeaders =
                                buildList {
                                    add("Content-Type" to listOf(ContentType.Application.Json.toString()))
                                    if (status == HttpStatusCode.TooManyRequests) {
                                        add(
                                            "X-Ratelimit" to
                                                listOf(
                                                    """{"name":"UNAUTHED_API_GET_LIMIT","period":300,"limit":1000,"remaining":0,"until":"2020-10-10T00:24:00Z"}""",
                                                ),
                                        )
                                    }
                                    retryAfterSeconds?.let {
                                        add("Retry-After" to listOf(it.toString()))
                                    }
                                }.let { headersOf(*it.toTypedArray()) }

                            respondError(
                                status = status,
                                headers = errorHeaders,
                            )
                        } else {
                            val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

                            respond(content = result, headers = headers)
                        }
                    }
                }
            }
        }
}
