package app.moviebase.trakt.core

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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
) : RuntimeException(buildMessage(statusCode, requestUrl, errorBody)) {

    val isNotFound: Boolean get() = statusCode == 404
    val isUnauthorized: Boolean get() = statusCode == 401
    val isForbidden: Boolean get() = statusCode == 403
    val isConflict: Boolean get() = statusCode == 409
    val isRateLimited: Boolean get() = statusCode == 429
    val isAccountLimitExceeded: Boolean get() = statusCode == 420
    val isServerError: Boolean get() = statusCode in 500..599
    val isClientError: Boolean get() = statusCode in 400..499
    val isRetryable: Boolean get() = isServerError || isRateLimited

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
