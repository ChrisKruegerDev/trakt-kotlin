package app.moviebase.trakt.url

import com.google.common.truth.Truth.assertThat
import io.ktor.http.Url
import org.junit.jupiter.api.Test

class TraktAuthenticationUrlBuilderTest {

    @Test
    fun `builds an encoded authorization url on the auth host with pkce`() {
        val url = Url(
            TraktAuthenticationUrlBuilder.buildAuthorizationUrl(
                clientId = "client id",
                redirectUri = "moviebase://auth.trakt",
                state = "state",
                codeChallenge = "challenge",
            ),
        )

        assertThat(url.host).isEqualTo("auth.trakt.tv")
        assertThat(url.encodedPath).isEqualTo("/oauth/authorize")
        assertThat(url.parameters["client_id"]).isEqualTo("client id")
        assertThat(url.parameters["redirect_uri"]).isEqualTo("moviebase://auth.trakt")
        assertThat(url.parameters["state"]).isEqualTo("state")
        assertThat(url.parameters["code_challenge"]).isEqualTo("challenge")
        assertThat(url.parameters["code_challenge_method"]).isEqualTo("S256")
    }
}
