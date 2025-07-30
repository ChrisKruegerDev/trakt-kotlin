package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktListMediaType
import app.moviebase.trakt.model.TraktListType
import app.moviebase.trakt.model.TraktMediaItem
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktSyncResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktSyncApi(
    private val client: HttpClient,
) {
    suspend fun addWatchedHistory(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("history")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun removeWatchedHistory(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("history", "remove")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun addToWatchlist(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("watchlist")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun removeFromWatchlist(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("watchlist", "remove")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun addToCollection(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("collection")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun removeFromCollection(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("collection", "remove")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun rateItems(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("ratings")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun removeRatings(items: TraktSyncItems): TraktSyncResponse =
        client
            .post {
                endPointSync("ratings", "remove")
                contentType(ContentType.Application.Json)
                setBody(items)
            }.body()

    suspend fun getSyncList(
        listType: TraktListType,
        mediaType: TraktListMediaType,
        itemId: Int? = null,
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktMediaItem> =
        client
            .get {
                endPointSyncList(listType, mediaType, itemId)
                extended?.let { parameterExtended(it) }
                page?.let { parameterPage(it) }
                limit?.let { parameterLimit(it) }
            }.body()

    suspend inline fun getWatchedShows(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.WATCHED,
            mediaType = TraktListMediaType.SHOWS,
            extended = extended,
        )

    suspend inline fun getWatchedMovies(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.WATCHED,
            mediaType = TraktListMediaType.MOVIES,
            extended = extended,
        )

    suspend inline fun getWatchlistMovies(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.WATCHLIST,
            mediaType = TraktListMediaType.MOVIES,
            extended = extended,
        )

    suspend inline fun getWatchlistShows(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.WATCHLIST,
            mediaType = TraktListMediaType.SHOWS,
            extended = extended,
        )

    suspend inline fun getWatchlistSeasons(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.WATCHLIST,
            mediaType = TraktListMediaType.SEASONS,
            extended = extended,
        )

    suspend inline fun getWatchlistEpisodes(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.WATCHLIST,
            mediaType = TraktListMediaType.EPISODES,
            extended = extended,
        )

    suspend inline fun getCollectionMovies(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.COLLECTION,
            mediaType = TraktListMediaType.MOVIES,
            extended = extended,
        )

    suspend inline fun getCollectionShows(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.COLLECTION,
            mediaType = TraktListMediaType.SHOWS,
            extended = extended,
        )

    suspend inline fun getRatedMovies(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.RATINGS,
            mediaType = TraktListMediaType.MOVIES,
            extended = extended,
        )

    suspend inline fun getRatedShows(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.RATINGS,
            mediaType = TraktListMediaType.SHOWS,
            extended = extended,
        )

    suspend inline fun getRatedEpisodes(extended: TraktExtended? = null): List<TraktMediaItem> =
        getSyncList(
            listType = TraktListType.RATINGS,
            mediaType = TraktListMediaType.EPISODES,
            extended = extended,
        )

    private fun HttpRequestBuilder.endPointSync(vararg paths: String) {
        endPoint("sync", *paths)
    }

    private fun HttpRequestBuilder.endPointSyncList(
        listType: TraktListType,
        mediaType: TraktListMediaType,
        itemId: Int? = null,
    ) {
        val pathSegments = listOfNotNull("sync", listType.value, mediaType.value, itemId?.toString())
        endPoint(*pathSegments.toTypedArray())
    }
}
