package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.TraktWebConfig
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterEndAt
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.core.parameterStartAt
import app.moviebase.trakt.model.TraktCollectionItem
import app.moviebase.trakt.model.TraktFavoriteItem
import app.moviebase.trakt.model.TraktFollowRequest
import app.moviebase.trakt.model.TraktFollowResponse
import app.moviebase.trakt.model.TraktHiddenItem
import app.moviebase.trakt.model.TraktHiddenSection
import app.moviebase.trakt.model.TraktHistoryItem
import app.moviebase.trakt.model.TraktLike
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktListMediaType
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktRatedItem
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktSyncResponse
import app.moviebase.trakt.model.TraktUser
import app.moviebase.trakt.model.TraktUserListItem
import app.moviebase.trakt.model.TraktUserSettings
import app.moviebase.trakt.model.TraktUserSlug
import app.moviebase.trakt.model.TraktWatchedItem
import app.moviebase.trakt.model.TraktWatching
import app.moviebase.trakt.model.TraktWatchlistItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.time.Instant

class TraktUsersApi(
    private val client: HttpClient,
) {
    suspend fun getSettings(): TraktUserSettings = client.get {
        endPoint("users")
    }.body()

    suspend fun getProfile(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): TraktUser = client.get {
        endPointUsers(userSlug)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun createList(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        list: TraktList,
    ): TraktList = client.post {
        endPointUsers(userSlug, "lists")
        contentType(ContentType.Application.Json)
        setBody(list)
    }.body()

    suspend fun getLists(userSlug: TraktUserSlug = TraktUserSlug.ME): List<TraktList> = client.get {
        endPointUsers(userSlug, "lists")
    }.body()

    suspend fun getListItems(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktUserListItem> = client.get {
        endPointLists(userSlug, listId)
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun addListItems(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
        items: TraktSyncItems,
    ): TraktSyncResponse = client.post {
        endPointLists(userSlug, listId)
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeListItems(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
        items: TraktSyncItems,
    ): TraktSyncResponse = client.post {
        endPointLists(userSlug, listId, "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    /**
     * Example: users/id/history/type/item_id?start_at=2016-06-01T00%3A00%3A00.000Z&end_at=2016-07-01T23%3A59%3A59.000Z
     */
    suspend fun getHistory(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listType: TraktListMediaType? = null,
        itemId: Int? = null,
        extended: TraktExtended? = null,
        startAt: Instant? = null,
        endAt: Instant? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktHistoryItem> = client.get {
        endPointHistory(userSlug, listType, itemId)
        extended?.let { parameterExtended(extended) }
        startAt?.let { parameterStartAt(it) }
        endAt?.let { parameterEndAt(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getFollowers(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktUser> = client.get {
        endPointUsers(userSlug, "followers")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getFollowing(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktUser> = client.get {
        endPointUsers(userSlug, "following")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getFriends(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktUser> = client.get {
        endPointUsers(userSlug, "friends")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getStats(userSlug: TraktUserSlug): TraktUser = client.get {
        endPointUsers(userSlug, "stats")
    }.body()

    suspend fun getWatching(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        extended: TraktExtended? = null,
    ): TraktWatching? {
        val response = client.get {
            endPointUsers(userSlug, "watching")
            extended?.let { parameterExtended(it) }
        }
        if (response.status == HttpStatusCode.NoContent) return null
        return response.body()
    }

    suspend fun getWatchedMovies(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktWatchedItem> = client.get {
        endPointUsers(userSlug, "watched", "movies")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getWatchedShows(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktWatchedItem> = client.get {
        endPointUsers(userSlug, "watched", "shows")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getCollectionMovies(
        userSlug: TraktUserSlug,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktCollectionItem> = client.get {
        endPointUsers(userSlug, "collection", "movies")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getCollectionShows(
        userSlug: TraktUserSlug,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktCollectionItem> = client.get {
        endPointUsers(userSlug, "collection", "shows")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun follow(userSlug: TraktUserSlug): TraktFollowResponse = client.post {
        endPointUsers(userSlug, "follow")
    }.body()

    suspend fun unfollow(userSlug: TraktUserSlug) = client.delete {
        endPointUsers(userSlug, "follow")
    }

    suspend fun getFollowerRequests(
        extended: TraktExtended? = null,
    ): List<TraktFollowRequest> = client.get {
        endPoint("users", "requests")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun approveFollowRequest(requestId: Int): TraktFollowResponse = client.post {
        endPoint("users", "requests", requestId.toString())
    }.body()

    suspend fun denyFollowRequest(requestId: Int) = client.delete {
        endPoint("users", "requests", requestId.toString())
    }

    suspend fun getHiddenItems(
        section: TraktHiddenSection,
        type: TraktMediaType? = null,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktHiddenItem> = client.get {
        if (type != null) {
            endPoint("users", "hidden", section.value, type.value)
        } else {
            endPoint("users", "hidden", section.value)
        }
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun addHiddenItems(
        section: TraktHiddenSection,
        items: TraktSyncItems,
    ): TraktSyncResponse = client.post {
        endPoint("users", "hidden", section.value)
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeHiddenItems(
        section: TraktHiddenSection,
        items: TraktSyncItems,
    ): TraktSyncResponse = client.post {
        endPoint("users", "hidden", section.value, "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun getLikes(
        type: String? = null,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
    ): List<TraktLike> = client.get {
        if (type != null) {
            endPoint("users", "likes", type)
        } else {
            endPoint("users", "likes")
        }
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun updateList(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
        list: TraktList,
    ): TraktList = client.put {
        endPoint("users", userSlug.name, "lists", listId)
        contentType(ContentType.Application.Json)
        setBody(list)
    }.body()

    suspend fun deleteList(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
    ) = client.delete {
        endPoint("users", userSlug.name, "lists", listId)
    }

    /**
     * Get a user's favorite movies.
     */
    suspend fun getFavoriteMovies(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktFavoriteItem> = client.get {
        endPointUsers(userSlug, "favorites", "movies")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's favorite shows.
     */
    suspend fun getFavoriteShows(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktFavoriteItem> = client.get {
        endPointUsers(userSlug, "favorites", "shows")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Add items to a user's favorites.
     */
    suspend fun addFavorites(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPoint("users", "me", "favorites")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    /**
     * Remove items from a user's favorites.
     */
    suspend fun removeFavorites(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPoint("users", "me", "favorites", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    /**
     * Get a user's ratings for movies.
     */
    suspend fun getRatingsMovies(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        rating: Int? = null,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktRatedItem> = client.get {
        if (rating != null) {
            endPointUsers(userSlug, "ratings", "movies", rating.toString())
        } else {
            endPointUsers(userSlug, "ratings", "movies")
        }
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's ratings for shows.
     */
    suspend fun getRatingsShows(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        rating: Int? = null,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktRatedItem> = client.get {
        if (rating != null) {
            endPointUsers(userSlug, "ratings", "shows", rating.toString())
        } else {
            endPointUsers(userSlug, "ratings", "shows")
        }
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's ratings for seasons.
     */
    suspend fun getRatingsSeasons(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        rating: Int? = null,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktRatedItem> = client.get {
        if (rating != null) {
            endPointUsers(userSlug, "ratings", "seasons", rating.toString())
        } else {
            endPointUsers(userSlug, "ratings", "seasons")
        }
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's ratings for episodes.
     */
    suspend fun getRatingsEpisodes(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        rating: Int? = null,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktRatedItem> = client.get {
        if (rating != null) {
            endPointUsers(userSlug, "ratings", "episodes", rating.toString())
        } else {
            endPointUsers(userSlug, "ratings", "episodes")
        }
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's watchlist movies.
     */
    suspend fun getWatchlistMovies(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointUsers(userSlug, "watchlist", "movies")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's watchlist shows.
     */
    suspend fun getWatchlistShows(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointUsers(userSlug, "watchlist", "shows")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's watchlist seasons.
     */
    suspend fun getWatchlistSeasons(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointUsers(userSlug, "watchlist", "seasons")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Get a user's watchlist episodes.
     */
    suspend fun getWatchlistEpisodes(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        page: Int = 1,
        limit: Int = TraktWebConfig.MAX_LIMIT_ITEMS,
        extended: TraktExtended? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointUsers(userSlug, "watchlist", "episodes")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Path: users/userSlug
     */
    private fun HttpRequestBuilder.endPointUsers(
        userSlug: TraktUserSlug,
        vararg paths: String,
    ) {
        endPoint("users", userSlug.name, *paths)
    }

    /**
     * Path: /users/userSlug/history/type/item_id
     */
    private fun HttpRequestBuilder.endPointHistory(
        userSlug: TraktUserSlug,
        listType: TraktListMediaType?,
        itemId: Int?,
        vararg paths: String,
    ) {
        val pathSegments = listOfNotNull("users", userSlug.name, "history", listType?.value, itemId?.toString(), *paths)
        endPoint(*pathSegments.toTypedArray())
    }

    /**
     * Path: users/{userSlug}/lists/{id}/items
     */
    private fun HttpRequestBuilder.endPointLists(
        userSlug: TraktUserSlug,
        listId: String,
        vararg paths: String,
    ) {
        endPoint("users", userSlug.name, "lists", listId, "items", *paths)
    }
}
