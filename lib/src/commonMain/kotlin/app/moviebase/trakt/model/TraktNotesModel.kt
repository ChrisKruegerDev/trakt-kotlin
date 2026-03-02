package app.moviebase.trakt.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktNote(
    @SerialName("id") val id: Long = 0,
    @SerialName("notes") val notes: String = "",
    @SerialName("spoiler") val spoiler: Boolean = false,
    @SerialName("privacy") val privacy: String = "private",
    @SerialName("created_at") val createdAt: Instant? = null,
    @SerialName("updated_at") val updatedAt: Instant? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("person") val person: TraktPerson? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("attached_to") val attachedTo: String? = null,
)

@Serializable
data class TraktNoteRequest(
    @SerialName("notes") val notes: String,
    @SerialName("spoiler") val spoiler: Boolean = false,
    @SerialName("privacy") val privacy: String = "private",
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("person") val person: TraktPerson? = null,
)
