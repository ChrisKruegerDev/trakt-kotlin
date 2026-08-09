package app.moviebase.trakt.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class TraktCheckin {

    @Serializable
    data class Active(
        @SerialName("id") val id: Long? = null,
        @SerialName("watched_at") val watchedAt: Instant? = null,
    ) : TraktCheckin()

    @Serializable
    data class Error(
        @SerialName("expires_at") val expiresAt: Instant? = null,
    ) : TraktCheckin()
}

@Serializable
data class TraktCheckinItem(
    val movie: TraktCheckinMovie? = null,
    val show: TraktCheckinShow? = null,
    val episode: TraktEpisode? = null,
    val sharing: TraktSharing? = null,
    val message: String? = null,
)

@Serializable
data class TraktCheckinMovie(
    @SerialName("title") val title: String? = null,
    @SerialName("ids") val ids: TraktItemIds,
)

@Serializable
data class TraktCheckinShow(
    @SerialName("title") val title: String? = null,
    @SerialName("ids") val ids: TraktItemIds,
)

@Serializable
data class TraktSharing(
    val twitter: Boolean? = null,
    val mastodon: Boolean? = null,
    val tumblr: Boolean? = null,
)
