package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TraktGrantType(
    val value: String,
) {
    @SerialName("refresh_token")
    REFRESH_TOKEN("refresh_token"),

    @SerialName("authorization_code")
    AUTHORIZATION_CODE("authorization_code"),
}

@Serializable
data class TraktAccessToken(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("expires_in") val expiresIn: Int? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("scope") val scope: String? = null,
    @SerialName("created_at") val createdAt: Long? = null,
)

@Serializable
data class TraktTokenRefreshRequest(
    @SerialName("client_id") val clientId: String? = null,
    @SerialName("client_secret") val clientSecret: String? = null,
    @SerialName("redirect_uri") val redirectUri: String? = null,
    @SerialName("grant_type") val grantType: TraktGrantType? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("code") val code: String? = null,
)

/**
 * Request body for device code generation.
 */
@Serializable
data class TraktDeviceCodeRequest(
    @SerialName("client_id") val clientId: String,
)

/**
 * Response from device code generation endpoint.
 */
@Serializable
data class TraktDeviceCode(
    @SerialName("device_code") val deviceCode: String,
    @SerialName("user_code") val userCode: String,
    @SerialName("verification_url") val verificationUrl: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("interval") val interval: Int,
)

/**
 * Request body for polling device token.
 */
@Serializable
data class TraktDeviceTokenRequest(
    @SerialName("code") val code: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("client_secret") val clientSecret: String? = null,
)

/**
 * Request body for revoking a token.
 */
@Serializable
data class TraktTokenRevokeRequest(
    @SerialName("token") val token: String,
    @SerialName("client_id") val clientId: String? = null,
    @SerialName("client_secret") val clientSecret: String? = null,
)
