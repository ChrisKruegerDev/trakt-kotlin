package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktCollectionItem
import app.moviebase.trakt.model.TraktFavoriteItem
import app.moviebase.trakt.model.TraktLastActivities
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktRatedItem
import app.moviebase.trakt.model.TraktWatchedItem
import app.moviebase.trakt.model.TraktWatchlistItem
import app.moviebase.trakt.model.TraktPlaybackItem
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktSyncResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktSyncApi(
    private val client: HttpClient,
) {

    suspend fun addWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("history")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("history", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun addToWatchlist(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("watchlist")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeFromWatchlist(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("watchlist", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun addToCollection(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("collection")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeFromCollection(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("collection", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun rateItems(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("ratings")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeRatings(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("ratings", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun addToFavorites(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("favorites")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeFromFavorites(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("favorites", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    // Watched endpoints

    suspend fun getWatchedShows(
        extended: TraktExtended? = null,
    ): List<TraktWatchedItem> = client.get {
        endPointSync("watched", "shows")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getWatchedMovies(
        extended: TraktExtended? = null,
    ): List<TraktWatchedItem> = client.get {
        endPointSync("watched", "movies")
        extended?.let { parameterExtended(it) }
    }.body()

    // Watchlist endpoints

    suspend fun getWatchlistMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getWatchlistShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getWatchlistSeasons(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "seasons")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getWatchlistEpisodes(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "episodes")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    // Collection endpoints

    suspend fun getCollectionMovies(
        extended: TraktExtended? = null,
    ): List<TraktCollectionItem> = client.get {
        endPointSync("collection", "movies")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getCollectionShows(
        extended: TraktExtended? = null,
    ): List<TraktCollectionItem> = client.get {
        endPointSync("collection", "shows")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getFavoriteMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktFavoriteItem> = client.get {
        endPointSync("favorites", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getFavoriteShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktFavoriteItem> = client.get {
        endPointSync("favorites", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    // Ratings endpoints

    suspend fun getRatedMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = client.get {
        endPointSync("ratings", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getRatedShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = client.get {
        endPointSync("ratings", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getRatedSeasons(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = client.get {
        endPointSync("ratings", "seasons")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getRatedEpisodes(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = client.get {
        endPointSync("ratings", "episodes")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getLastActivities(): TraktLastActivities = client.get {
        endPointSync("last_activities")
    }.body()

    suspend fun getPlaybackProgress(
        type: TraktMediaType? = null,
        limit: Int? = null,
    ): List<TraktPlaybackItem> = client.get {
        if (type != null) {
            endPointSync("playback", type.value)
        } else {
            endPointSync("playback")
        }
        limit?.let { parameter("limit", it) }
    }.body()

    suspend fun removePlaybackProgress(playbackId: Int) = client.delete {
        endPointSync("playback", playbackId.toString())
    }

    private fun HttpRequestBuilder.endPointSync(vararg paths: String) {
        endPoint("sync", *paths)
    }
}
