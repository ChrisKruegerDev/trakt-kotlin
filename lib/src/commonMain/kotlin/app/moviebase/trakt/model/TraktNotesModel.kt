package app.moviebase.trakt.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A note resource as returned by the single-note endpoints (`POST /notes`, `GET /notes/{id}`,
 * `PUT /notes/{id}`). The list endpoint (`GET /users/{id}/notes`) wraps this inside [TraktNoteItem.note].
 */
@Serializable
data class TraktNote(
    @SerialName("id") val id: Long = 0,
    @SerialName("notes") val notes: String = "",
    @SerialName("spoiler") val spoiler: Boolean = false,
    @SerialName("privacy") val privacy: String = "private",
    @SerialName("created_at") val createdAt: Instant? = null,
    @SerialName("updated_at") val updatedAt: Instant? = null,
)

/**
 * A single entry of the notes list endpoint (`GET /users/{id}/notes`) and the attached-item endpoint
 * (`GET /notes/{id}/item`). It pairs the [attachedTo] context and the media item ([movie]/[show]/…) with
 * the actual [note]. The note content is nested under `note`, NOT flattened into this object.
 */
@Serializable
data class TraktNoteItem(
    @SerialName("attached_to") val attachedTo: TraktNoteAttachedTo? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("person") val person: TraktPerson? = null,
    @SerialName("note") val note: TraktNote? = null,
)

/**
 * Describes what a note is attached to, e.g. `{"type":"movie"}` or `{"type":"history","id":4943432}`.
 */
@Serializable
data class TraktNoteAttachedTo(
    @SerialName("type") val type: String? = null,
    @SerialName("id") val id: Long? = null,
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
