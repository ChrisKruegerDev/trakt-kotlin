package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktAnticipatedShow
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktShow
import app.moviebase.trakt.model.TraktTrendingShow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TraktShowsApi(
    private val client: HttpClient,
) {
    suspend fun getTrending(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktTrendingShow> =
        client
            .get {
                endPointShows("trending")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getPopular(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> =
        client
            .get {
                endPointShows("popular")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getAnticipated(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktAnticipatedShow> =
        client
            .get {
                endPointShows("anticipated")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getPlayed(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> =
        client
            .get {
                endPointShows("played")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getWatched(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> =
        client
            .get {
                endPointShows("watched")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getCollected(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> =
        client
            .get {
                endPointShows("collected")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getRelated(
        showId: String,
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> =
        client
            .get {
                endPointShow(showId, "related")
                parameterPage(page)
                parameterLimit(limit)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getSummary(
        showId: String,
        extended: TraktExtended? = null,
    ): TraktShow =
        client
            .get {
                endPointShows(showId)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getRating(traktSlug: String): TraktRating =
        client
            .get {
                endPointShow(traktSlug, "ratings")
            }.body()

    suspend fun getStats(traktSlug: String): TraktRating =
        client
            .get {
                endPointShow(traktSlug, "stats")
            }.body()

    suspend fun getTranslations(
        showId: String,
        language: String? = null,
    ): List<TraktShow> =
        client
            .get {
                endPointShow(showId, "translations", language ?: "")
            }.body()

    suspend fun getComments(
        showId: String,
        sort: String = "newest",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktShow> =
        client
            .get {
                endPointShow(showId, "comments", sort)
                parameterPage(page)
                parameterLimit(limit)
            }.body()

    suspend fun getProgress(
        showId: String,
        hidden: Boolean = false,
        specials: Boolean = false,
        countSpecials: Boolean = true,
    ): TraktShow =
        client
            .get {
                endPointShow(showId, "progress", "watched")
                parameter("hidden", hidden)
                parameter("specials", specials)
                parameter("count_specials", countSpecials)
            }.body()

    private fun HttpRequestBuilder.endPointShow(
        traktSlug: String,
        vararg paths: String,
    ) {
        endPoint("shows", traktSlug, *paths)
    }

    private fun HttpRequestBuilder.endPointShows(vararg paths: String) {
        endPoint("shows", *paths)
    }
}
