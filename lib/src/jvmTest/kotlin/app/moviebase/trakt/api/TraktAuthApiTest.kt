package app.moviebase.trakt.api

import app.moviebase.trakt.TraktClientConfig
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Test

class TraktAuthApiTest {

    @Test
    fun `routes every oauth operation through the auth host and sends the pkce verifier`() = runTest {
        val requests = mutableListOf<HttpRequestData>()
        val client = HttpClient(MockEngine) {
            install(ContentNegotiation) { json() }
            engine {
                addHandler { request ->
                    requests += request
                    val responseBody = when (request.url.encodedPath) {
                        "/oauth/device/code" ->
                            """{"device_code":"device","user_code":"user","verification_url":"https://trakt.tv/activate","expires_in":600,"interval":5}"""
                        "/oauth/revoke" -> ""
                        else ->
                            """{"access_token":"access","refresh_token":"refresh","expires_in":604800,"created_at":1}"""
                    }
                    respond(
                        content = responseBody,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                    )
                }
            }
        }
        val api = TraktAuthApi(
            client = client,
            config = TraktClientConfig().apply {
                clientId = "client"
                clientSecret = "secret"
            },
        )

        api.requestAccessToken("moviebase://auth.trakt", "code", "verifier")
        api.requestRefreshToken("moviebase://auth.trakt", "refresh")
        api.generateDeviceCode()
        api.pollDeviceToken("device")
        api.revokeToken("access")

        assertThat(requests.map { it.url.host }.distinct()).containsExactly("auth.trakt.tv")
        assertThat(requests.map { it.url.encodedPath }).containsExactly(
            "/oauth/token",
            "/oauth/token",
            "/oauth/device/code",
            "/oauth/device/token",
            "/oauth/revoke",
        ).inOrder()

        val body = requests.first().body as OutgoingContent.ByteArrayContent
        val json = Json.parseToJsonElement(body.bytes().decodeToString()).jsonObject
        assertThat(json.getValue("code_verifier").jsonPrimitive.content).isEqualTo("verifier")
    }
}
