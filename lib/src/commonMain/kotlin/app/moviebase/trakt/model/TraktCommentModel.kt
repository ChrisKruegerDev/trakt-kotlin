package app.moviebase.trakt.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class TraktCommentSort(val value: String) {
    NEWEST("newest"),
    OLDEST("oldest"),
    LIKES("likes"),
    REPLIES("replies"),
}

@Serializable
data class TraktPostComment(
    @SerialName("comment") val comment: String,
    @SerialName("spoiler") val spoiler: Boolean,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("season") val season: TraktSeason? = null,
)

@Serializable
data class TraktComment(
    @SerialName("id") val id: Int,
    @SerialName("parent_id") val parentId: Int? = null,
    @SerialName("created_at") val createdAt: Instant? = null,
    @SerialName("updated_at") val updatedAt: Instant? = null,
    @SerialName("comment") val comment: String,
    @SerialName("spoiler") val spoiler: Boolean,
    @SerialName("review") val review: Boolean = false,
    @SerialName("language") val language: String? = null,
    @SerialName("likes") val likes: Int? = null,
    @SerialName("replies") val replies: Int? = null,
    @SerialName("user_rating") val userRating: Int? = null,
    @SerialName("user") val user: TraktUser?,
    @SerialName("user_stats") val userStats: TraktCommentUserStats? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("season") val season: TraktSeason? = null,
) {
    val displayUserName get() = user?.name ?: user?.userName
    val imagePath get() = user?.imagePath
    val containsSpoiler get() = comment.contains("[spoiler]")
}

/**
 * Stats of the commenting user for the item the comment is attached to.
 */
@Serializable
data class TraktCommentUserStats(
    @SerialName("rating") val rating: Int? = null,
    @SerialName("play_count") val playCount: Int = 0,
    @SerialName("completed_count") val completedCount: Int = 0,
)
