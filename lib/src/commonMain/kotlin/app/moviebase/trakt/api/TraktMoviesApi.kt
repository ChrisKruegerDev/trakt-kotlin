package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktAnticipatedMovie
import app.moviebase.trakt.model.TraktMovie
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktTrendingMovie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktMoviesApi(
    private val client: HttpClient,
) {
    suspend fun getTrending(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktTrendingMovie> =
        client
            .get {
                endPointMovies("trending")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getPopular(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovie> =
        client
            .get {
                endPointMovies("popular")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getAnticipated(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktAnticipatedMovie> =
        client
            .get {
                endPointMovies("anticipated")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getPlayed(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovie> =
        client
            .get {
                endPointMovies("played")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getWatched(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovie> =
        client
            .get {
                endPointMovies("watched")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getCollected(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovie> =
        client
            .get {
                endPointMovies("collected")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getRelated(
        movieId: String,
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovie> =
        client
            .get {
                endPointMovie(movieId, "related")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getSummary(
        traktSlug: String,
        extended: TraktExtended? = null,
    ): TraktMovie =
        client
            .get {
                endPointMovies(traktSlug)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getRating(traktSlug: String): TraktRating =
        client
            .get {
                endPointMovie(traktSlug, "ratings")
            }.body()

    suspend fun getStats(traktSlug: String): TraktRating =
        client
            .get {
                endPointMovie(traktSlug, "stats")
            }.body()

    private fun HttpRequestBuilder.endPointMovie(
        movieId: String,
        vararg paths: String,
    ) {
        endPoint("movies", movieId, *paths)
    }

    private fun HttpRequestBuilder.endPointMovies(vararg paths: String) {
        endPoint("movies", *paths)
    }
}
