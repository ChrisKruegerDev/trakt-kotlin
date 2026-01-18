package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktRating(
    @SerialName("rating") val rating: Double,
    @SerialName("votes") val votes: Int,
    @SerialName("distribution") val distribution: TraktRatingDistribution? = null,
)

@Serializable
data class TraktRatingDistribution(
    @SerialName("1") val rating1: Int = 0,
    @SerialName("2") val rating2: Int = 0,
    @SerialName("3") val rating3: Int = 0,
    @SerialName("4") val rating4: Int = 0,
    @SerialName("5") val rating5: Int = 0,
    @SerialName("6") val rating6: Int = 0,
    @SerialName("7") val rating7: Int = 0,
    @SerialName("8") val rating8: Int = 0,
    @SerialName("9") val rating9: Int = 0,
    @SerialName("10") val rating10: Int = 0,
)

/**
 * Statistics for a movie, show, season, or episode.
 */
@Serializable
data class TraktStats(
    @SerialName("watchers") val watchers: Int = 0,
    @SerialName("plays") val plays: Int = 0,
    @SerialName("collectors") val collectors: Int = 0,
    @SerialName("collected_episodes") val collectedEpisodes: Int = 0,
    @SerialName("comments") val comments: Int = 0,
    @SerialName("lists") val lists: Int = 0,
    @SerialName("votes") val votes: Int = 0,
    @SerialName("favorited") val favorited: Int = 0,
)
