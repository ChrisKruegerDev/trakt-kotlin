package app.moviebase.trakt.core

import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TraktExceptionTest {

    private val json = JsonFactory.create()

    @Test
    fun `message includes status code and url`() {
        val exception = TraktException(
            statusCode = 404,
            requestUrl = "https://api.trakt.tv/movies/unknown",
            body = "",
        )

        assertThat(exception.message).isEqualTo(
            "Trakt API error 404 for https://api.trakt.tv/movies/unknown",
        )
    }

    @Test
    fun `message includes error description when present`() {
        val exception = TraktException(
            statusCode = 401,
            requestUrl = "https://api.trakt.tv/oauth/token",
            body = """{"error":"invalid_grant","error_description":"The provided authorization grant is invalid"}""",
            errorBody = TraktErrorBody(
                error = "invalid_grant",
                errorDescription = "The provided authorization grant is invalid",
            ),
        )

        assertThat(exception.message).isEqualTo(
            "Trakt API error 401 for https://api.trakt.tv/oauth/token: The provided authorization grant is invalid",
        )
    }

    @Test
    fun `message falls back to error field when no description`() {
        val exception = TraktException(
            statusCode = 401,
            requestUrl = "https://api.trakt.tv/oauth/token",
            body = """{"error":"invalid_grant"}""",
            errorBody = TraktErrorBody(error = "invalid_grant"),
        )

        assertThat(exception.message).isEqualTo(
            "Trakt API error 401 for https://api.trakt.tv/oauth/token: invalid_grant",
        )
    }

    @Test
    fun `parseErrorBody returns body for valid JSON`() {
        val body = """{"error":"invalid_grant","error_description":"The provided authorization grant is invalid"}"""
        val result = parseErrorBody(json, body)

        assertThat(result).isNotNull()
        assertThat(result?.error).isEqualTo("invalid_grant")
        assertThat(result?.errorDescription).isEqualTo("The provided authorization grant is invalid")
    }

    @Test
    fun `parseErrorBody returns null for empty body`() {
        assertThat(parseErrorBody(json, "")).isNull()
        assertThat(parseErrorBody(json, "  ")).isNull()
    }

    @Test
    fun `parseErrorBody returns null for non-JSON body`() {
        assertThat(parseErrorBody(json, "Not found")).isNull()
    }

    @Test
    fun `mock engine throws TraktException on 404`() = runTest {
        val client = mockErrorClient(HttpStatusCode.NotFound, "")

        val exception = assertThrows<TraktException> {
            client.get("https://api.trakt.tv/movies/unknown")
        }

        assertThat(exception.statusCode).isEqualTo(404)
        assertThat(exception.requestUrl).isEqualTo("https://api.trakt.tv/movies/unknown")
        assertThat(exception.body).isEmpty()
        assertThat(exception.errorBody).isNull()
    }

    @Test
    fun `mock engine throws TraktException on 401 with JSON error body`() = runTest {
        val errorJson = """{"error":"invalid_grant","error_description":"The provided authorization grant is invalid"}"""
        val client = mockErrorClient(HttpStatusCode.Unauthorized, errorJson)

        val exception = assertThrows<TraktException> {
            client.get("https://api.trakt.tv/oauth/token")
        }

        assertThat(exception.statusCode).isEqualTo(401)
        assertThat(exception.requestUrl).isEqualTo("https://api.trakt.tv/oauth/token")
        assertThat(exception.errorBody).isNotNull()
        assertThat(exception.errorBody?.error).isEqualTo("invalid_grant")
        assertThat(exception.errorBody?.errorDescription).isEqualTo("The provided authorization grant is invalid")
    }

    private fun mockErrorClient(status: HttpStatusCode, body: String): HttpClient {
        val json = JsonFactory.create()
        return HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json(json)
            }

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
                        )
                    }
                }
            }

            engine {
                addHandler {
                    respond(
                        content = body,
                        status = status,
                        headers = headersOf("Content-Type" to listOf("application/json")),
                    )
                }
            }
        }
    }
}
