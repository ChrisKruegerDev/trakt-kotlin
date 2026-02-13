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
