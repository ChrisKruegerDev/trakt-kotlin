package app.moviebase.trakt.api

import app.moviebase.trakt.TraktClientConfig
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktAccessToken
import app.moviebase.trakt.model.TraktDeviceCode
import app.moviebase.trakt.model.TraktDeviceCodeRequest
import app.moviebase.trakt.model.TraktDeviceTokenRequest
import app.moviebase.trakt.model.TraktGrantType
import app.moviebase.trakt.model.TraktTokenRefreshRequest
import app.moviebase.trakt.model.TraktTokenRevokeRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktAuthApi(
    private val client: HttpClient,
    private val config: TraktClientConfig,
) {

    suspend fun postToken(request: TraktTokenRefreshRequest): TraktAccessToken = client.post {
        endPointOAuth("token")
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()

    suspend fun requestAccessToken(
        redirectUri: String,
        code: String,
    ): TraktAccessToken {
        val requestToken =
            TraktTokenRefreshRequest(
                clientId = config.clientId,
                clientSecret = config.clientSecret,
                redirectUri = redirectUri,
                grantType = TraktGrantType.AUTHORIZATION_CODE,
                code = code,
            )
        return postToken(requestToken)
    }

    suspend fun requestRefreshToken(
        redirectUri: String,
        refreshToken: String,
    ): TraktAccessToken {
        require(refreshToken.isNotBlank()) { "refresh token is empty" }

        val requestToken =
            TraktTokenRefreshRequest(
                clientId = config.clientId,
                clientSecret = config.clientSecret,
                redirectUri = redirectUri,
                grantType = TraktGrantType.REFRESH_TOKEN,
                refreshToken = refreshToken,
            )
        return postToken(requestToken)
    }

    /**
     * Generate a device code for devices with limited input capabilities.
     * The user will then visit the verification_url and enter the user_code.
     *
     * @see [Device Authentication](https://trakt.docs.apiary.io/#reference/authentication-devices)
     */
    suspend fun generateDeviceCode(): TraktDeviceCode = client.post {
        endPointOAuth("device", "code")
        contentType(ContentType.Application.Json)
        setBody(TraktDeviceCodeRequest(clientId = config.clientId!!))
    }.body()

    /**
     * Poll this endpoint to get the access token once the user has authorized the device.
     * Use the interval from the device code response to determine polling frequency.
     *
     * Status codes:
     * - 200: Success - token returned
     * - 400: Pending - user hasn't authorized yet, keep polling
     * - 404: Not Found - invalid device_code
     * - 409: Already Used - user already approved this code
     * - 410: Expired - device_code has expired
     * - 418: Denied - user explicitly denied this code
     * - 429: Slow Down - polling too quickly
     *
     * @param deviceCode The device_code from the device code response
     * @see [Device Authentication](https://trakt.docs.apiary.io/#reference/authentication-devices)
     */
    suspend fun pollDeviceToken(deviceCode: String): TraktAccessToken = client.post {
        endPointOAuth("device", "token")
        contentType(ContentType.Application.Json)
        setBody(
            TraktDeviceTokenRequest(
                code = deviceCode,
                clientId = config.clientId!!,
                clientSecret = config.clientSecret,
            ),
        )
    }.body()

    /**
     * Revoke an access token when a user signs out.
     * This will invalidate the access_token and refresh_token.
     *
     * @param token The access_token to revoke
     * @see [Token Revocation](https://trakt.docs.apiary.io/#reference/authentication-oauth)
     */
    suspend fun revokeToken(token: String) {
        client.post {
            endPointOAuth("revoke")
            contentType(ContentType.Application.Json)
            setBody(
                TraktTokenRevokeRequest(
                    token = token,
                    clientId = config.clientId,
                    clientSecret = config.clientSecret,
                ),
            )
        }
    }

    private fun HttpRequestBuilder.endPointOAuth(vararg paths: String) {
        endPoint("oauth", *paths)
    }
}
