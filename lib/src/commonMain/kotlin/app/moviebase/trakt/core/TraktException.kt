package app.moviebase.trakt.core

import app.moviebase.trakt.TraktHeader
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class TraktErrorBody(
    val error: String? = null,
    @SerialName("error_description") val errorDescription: String? = null,
)

class TraktException(
    val statusCode: Int,
    val requestUrl: String,
    val body: String,
    val errorBody: TraktErrorBody? = null,
    val headers: Map<String, String> = emptyMap(),
) : RuntimeException(buildMessage(statusCode, requestUrl, errorBody)) {

    val isNotFound: Boolean get() = statusCode == 404
    val isUnauthorized: Boolean get() = statusCode == 401
    val isForbidden: Boolean get() = statusCode == 403
    val isConflict: Boolean get() = statusCode == 409
    val isPreconditionFailed: Boolean get() = statusCode == 412
    val isRateLimited: Boolean get() = statusCode == 429
    val isAccountLimitExceeded: Boolean get() = statusCode == 420
    val isValidationError: Boolean get() = statusCode == 422
    val isLockedUserAccount: Boolean get() = statusCode == 423
    val isVipOnly: Boolean get() = statusCode == 426
    val isServerError: Boolean get() = statusCode in 500..599
    val isClientError: Boolean get() = statusCode in 400..499
    val isRetryable: Boolean get() = isServerError || isRateLimited

    val retryAfterSeconds: Int? get() = header(TraktHeader.RETRY_AFTER)?.toIntOrNull()
    val retryAfter: Duration? get() = retryAfterSeconds?.seconds

    val upgradeUrl: String? get() = header(TraktHeader.X_UPGRADE_URL)
    val vipUser: Boolean? get() = header(TraktHeader.X_VIP_USER)?.toBooleanStrictOrNull()
    val accountLimit: String? get() = header(TraktHeader.X_ACCOUNT_LIMIT)

    fun header(name: String): String? {
        headers[name]?.let { return it }
        return headers.entries.firstOrNull { it.key.equals(name, ignoreCase = true) }?.value
    }

    companion object {
        private fun buildMessage(statusCode: Int, requestUrl: String, errorBody: TraktErrorBody?): String {
            val detail = errorBody?.errorDescription ?: errorBody?.error
            return if (detail != null) {
                "Trakt API error $statusCode for $requestUrl: $detail"
            } else {
                "Trakt API error $statusCode for $requestUrl"
            }
        }
    }
}

internal fun parseErrorBody(json: Json, body: String): TraktErrorBody? {
    if (body.isBlank()) return null
    return try {
        json.decodeFromString<TraktErrorBody>(body)
    } catch (_: Exception) {
        null
    }
}
