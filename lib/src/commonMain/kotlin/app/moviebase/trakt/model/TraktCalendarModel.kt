package app.moviebase.trakt.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktCalendarShow(
    @SerialName("first_aired") val firstAired: Instant? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("show") val show: TraktShow? = null,
)

@Serializable
data class TraktCalendarMovie(
    @SerialName("released") val released: String? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
)
