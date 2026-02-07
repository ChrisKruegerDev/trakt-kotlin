package app.moviebase.trakt.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktSyncItems(
    @SerialName("movies") var movies: List<TraktSyncMovie>? = null,
    @SerialName("shows") var shows: List<TraktSyncShow>? = null,
    @SerialName("episodes") var episodes: List<TraktSyncEpisode>? = null,
    @SerialName("people") var people: List<TraktSyncPerson>? = null,
    @SerialName("ids") var ids: List<Long>? = null,
)

sealed interface TraktSyncItem {
    val ids: TraktItemIds?
    val rating: Int?
    val watchedAt: Instant?
    val collectedAt: Instant?
    val ratedAt: Instant?
}

@Serializable
data class TraktSyncMovie(
    @SerialName("ids") override val ids: TraktItemIds,
    @SerialName("rating") override val rating: Int? = null,
    @SerialName("watched_at") override val watchedAt: Instant? = null,
    @SerialName("collected_at") override val collectedAt: Instant? = null,
    @SerialName("rated_at") override val ratedAt: Instant? = null,
) : TraktSyncItem

@Serializable
data class TraktSyncShow(
    @SerialName("ids") override val ids: TraktItemIds,
    @SerialName("rating") override val rating: Int? = null,
    @SerialName("watched_at") override val watchedAt: Instant? = null,
    @SerialName("collected_at") override val collectedAt: Instant? = null,
    @SerialName("rated_at") override val ratedAt: Instant? = null,
    @SerialName("seasons") val seasons: List<TraktSyncSeason>? = null,
) : TraktSyncItem

@Serializable
data class TraktSyncSeason(
    @SerialName("ids") override val ids: TraktItemIds? = null,
    @SerialName("number") val number: Int? = null,
    @SerialName("rating") override val rating: Int? = null,
    @SerialName("watched_at") override val watchedAt: Instant? = null,
    @SerialName("collected_at") override val collectedAt: Instant? = null,
    @SerialName("rated_at") override val ratedAt: Instant? = null,
    @SerialName("episodes") val episodes: List<TraktSyncEpisode> = emptyList(),
) : TraktSyncItem

@Serializable
data class TraktSyncEpisode(
    @SerialName("ids") val ids: TraktItemIds? = null,
    @SerialName("number") val number: Int? = null,
    @SerialName("rating") val rating: Int? = null,
    @SerialName("watched_at") val watchedAt: Instant? = null,
    @SerialName("collected_at") val collectedAt: Instant? = null,
    @SerialName("rated_at") val ratedAt: Instant? = null,
)

@Serializable
data class TraktSyncPerson(
    @SerialName("ids") val ids: TraktPersonIds,
    @SerialName("name") val name: String? = null,
)

@Serializable
data class TraktSyncResponse(
    @SerialName("added") val added: TraktSyncStats? = null,
    @SerialName("existing") val existing: TraktSyncStats? = null,
    @SerialName("deleted") val deleted: TraktSyncStats? = null,
    @SerialName("not_found") val notFound: TraktSyncErrors? = null,
)

@Serializable
data class TraktSyncStats(
    @SerialName("movies") val movies: Int = 0,
    @SerialName("shows") val shows: Int = 0,
    @SerialName("seasons") val seasons: Int = 0,
    @SerialName("episodes") val episodes: Int = 0,
) {
    val count: Int get() = movies + shows + seasons + episodes
}

@Serializable
data class TraktSyncErrors(
    @SerialName("movies") val movies: List<TraktSyncMovie> = emptyList(),
    @SerialName("shows") val shows: List<TraktSyncShow> = emptyList(),
    @SerialName("seasons") val seasons: List<TraktSyncSeason> = emptyList(),
    @SerialName("episodes") val episodes: List<TraktSyncEpisode> = emptyList(),
    @SerialName("people") val people: List<TraktSyncPerson> = emptyList(),
    @SerialName("ids") val ids: List<Long> = emptyList(),
) {
    val isEmpty: Boolean get() = listOf(movies, shows, seasons, episodes, people, ids).all { it.isEmpty() }
}

@Serializable
data class TraktLastActivities(
    @SerialName("all") val all: Instant,
    @SerialName("movies") val movies: TraktMovieActivities? = null,
    @SerialName("episodes") val episodes: TraktEpisodeActivities? = null,
    @SerialName("shows") val shows: TraktShowActivities? = null,
    @SerialName("seasons") val seasons: TraktSeasonActivities? = null,
    @SerialName("comments") val comments: TraktCommentActivities? = null,
    @SerialName("lists") val lists: TraktListActivities? = null,
    @SerialName("watchlist") val watchlist: TraktWatchlistActivities? = null,
    @SerialName("favorites") val favorites: TraktFavoritesActivities? = null,
    @SerialName("account") val account: TraktAccountActivities? = null,
    @SerialName("saved_filters") val savedFilters: TraktSavedFiltersActivities? = null,
    @SerialName("notes") val notes: TraktNotesActivities? = null,
)

@Serializable
data class TraktMovieActivities(
    @SerialName("watched_at") val watchedAt: Instant? = null,
    @SerialName("collected_at") val collectedAt: Instant? = null,
    @SerialName("rated_at") val ratedAt: Instant? = null,
    @SerialName("watchlisted_at") val watchlistedAt: Instant? = null,
    @SerialName("favorited_at") val favoritedAt: Instant? = null,
    @SerialName("recommendations_at") val recommendationsAt: Instant? = null,
    @SerialName("commented_at") val commentedAt: Instant? = null,
    @SerialName("paused_at") val pausedAt: Instant? = null,
    @SerialName("hidden_at") val hiddenAt: Instant? = null,
)

@Serializable
data class TraktEpisodeActivities(
    @SerialName("watched_at") val watchedAt: Instant? = null,
    @SerialName("collected_at") val collectedAt: Instant? = null,
    @SerialName("rated_at") val ratedAt: Instant? = null,
    @SerialName("watchlisted_at") val watchlistedAt: Instant? = null,
    @SerialName("commented_at") val commentedAt: Instant? = null,
    @SerialName("paused_at") val pausedAt: Instant? = null,
)

@Serializable
data class TraktShowActivities(
    @SerialName("rated_at") val ratedAt: Instant? = null,
    @SerialName("watchlisted_at") val watchlistedAt: Instant? = null,
    @SerialName("favorited_at") val favoritedAt: Instant? = null,
    @SerialName("recommendations_at") val recommendationsAt: Instant? = null,
    @SerialName("commented_at") val commentedAt: Instant? = null,
    @SerialName("hidden_at") val hiddenAt: Instant? = null,
)

@Serializable
data class TraktSeasonActivities(
    @SerialName("rated_at") val ratedAt: Instant? = null,
    @SerialName("watchlisted_at") val watchlistedAt: Instant? = null,
    @SerialName("commented_at") val commentedAt: Instant? = null,
    @SerialName("hidden_at") val hiddenAt: Instant? = null,
)

@Serializable
data class TraktCommentActivities(
    @SerialName("liked_at") val likedAt: Instant? = null,
    @SerialName("blocked_at") val blockedAt: Instant? = null,
)

@Serializable
data class TraktListActivities(
    @SerialName("liked_at") val likedAt: Instant? = null,
    @SerialName("updated_at") val updatedAt: Instant? = null,
    @SerialName("commented_at") val commentedAt: Instant? = null,
)

@Serializable
data class TraktWatchlistActivities(
    @SerialName("updated_at") val updatedAt: Instant? = null,
)

@Serializable
data class TraktFavoritesActivities(
    @SerialName("updated_at") val updatedAt: Instant? = null,
)

@Serializable
data class TraktAccountActivities(
    @SerialName("settings_at") val settingsAt: Instant? = null,
    @SerialName("followed_at") val followedAt: Instant? = null,
    @SerialName("following_at") val followingAt: Instant? = null,
    @SerialName("pending_at") val pendingAt: Instant? = null,
    @SerialName("requested_at") val requestedAt: Instant? = null,
)

@Serializable
data class TraktSavedFiltersActivities(
    @SerialName("updated_at") val updatedAt: Instant? = null,
)

@Serializable
data class TraktNotesActivities(
    @SerialName("updated_at") val updatedAt: Instant? = null,
)

@Serializable
data class TraktPlaybackItem(
    @SerialName("id") val id: Int,
    @SerialName("progress") val progress: Float,
    @SerialName("paused_at") val pausedAt: Instant,
    @SerialName("type") val type: TraktMediaType,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("show") val show: TraktShow? = null,
)
