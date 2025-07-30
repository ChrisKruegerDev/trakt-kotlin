package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterEndAt
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.core.parameterStartAt
import app.moviebase.trakt.model.TraktHistoryItem
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktListMediaType
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktSyncResponse
import app.moviebase.trakt.model.TraktUser
import app.moviebase.trakt.model.TraktUserListItem
import app.moviebase.trakt.model.TraktUserSettings
import app.moviebase.trakt.model.TraktUserSlug
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.datetime.Instant

class TraktUsersApi(
    private val client: HttpClient,
) {
    suspend fun getSettings(): TraktUserSettings =
        client
            .get {
                endPoint("users")
            }.body()

    suspend fun getProfile(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): TraktUser =
        client
            .get {
                endPointUsers(userSlug)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun createList(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        list: TraktList,
    ): TraktList =
        client
            .post {
                endPointUsers(userSlug, "lists")
                contentType(ContentType.Application.Json)
                setBody(list)
            }.body()

    suspend fun getLists(userSlug: TraktUserSlug = TraktUserSlug.ME): List<TraktList> =
        client
            .get {
                endPointUsers(userSlug, "lists")
            }.body()

    suspend fun getListItems(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
        extended: TraktExtended? = null,
    ): List<TraktUserListItem> =
        client
            .get {
                endPointLists(userSlug, listId)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun addListItems(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
        items: TraktSyncItems,
    ): TraktSyncResponse =
        client
            .post {
                endPointLists(userSlug, listId)
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun removeListItems(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listId: String,
        items: TraktSyncItems,
    ): TraktSyncResponse =
        client
            .post {
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
    ): List<TraktHistoryItem> =
        client
            .get {
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
    ): List<TraktUser> =
        client
            .get {
                endPointUsers(userSlug, "followers")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getFollowing(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktUser> =
        client
            .get {
                endPointUsers(userSlug, "following")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getFriends(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktUser> =
        client
            .get {
                endPointUsers(userSlug, "friends")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getStats(userSlug: TraktUserSlug): TraktUser =
        client
            .get {
                endPointUsers(userSlug, "stats")
            }.body()

    suspend fun getWatching(userSlug: TraktUserSlug): TraktHistoryItem =
        client
            .get {
                endPointUsers(userSlug, "watching")
            }.body()

    suspend fun getWatchedMovies(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktHistoryItem> =
        client
            .get {
                endPointUsers(userSlug, "watched", "movies")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getWatchedShows(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktHistoryItem> =
        client
            .get {
                endPointUsers(userSlug, "watched", "shows")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getCollectionMovies(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktHistoryItem> =
        client
            .get {
                endPointUsers(userSlug, "collection", "movies")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getCollectionShows(
        userSlug: TraktUserSlug,
        extended: TraktExtended? = null,
    ): List<TraktHistoryItem> =
        client
            .get {
                endPointUsers(userSlug, "collection", "shows")
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
