package app.moviebase.trakt.url

import app.moviebase.trakt.TraktWebConfig
import io.ktor.http.URLBuilder

object TraktAuthenticationUrlBuilder {
    /**
     * Build an OAuth 2.0 authorization URL to obtain an authorization code.
     * Send the user to the URL. Once the user authorized your app, the server will redirect to `redirectUri`
     * with the authorization code and the sent state in the query parameter `code`.
     * Ensure the state matches, then supply the authorization code to [.accessToken] to get an
     * access token.
     * Example: https://auth.trakt.tv/oauth/authorize?response_type=code&client_id=%20&redirect_uri=%20&state=%20
     *
     * @param state State variable to prevent request forgery attacks. State variable for CSRF (Cross Site Request Forgery) purposes.
     */
    fun buildAuthorizationUrl(
        clientId: String,
        redirectUri: String,
        state: String,
        codeChallenge: String? = null,
    ): String = URLBuilder(TraktWebConfig.OAUTH2_AUTHORIZATION_URL).apply {
        parameters.append("response_type", "code")
        parameters.append("client_id", clientId)
        parameters.append("redirect_uri", redirectUri)
        parameters.append("state", state)
        codeChallenge?.let {
            parameters.append("code_challenge", it)
            parameters.append("code_challenge_method", "S256")
        }
    }.buildString()
}
