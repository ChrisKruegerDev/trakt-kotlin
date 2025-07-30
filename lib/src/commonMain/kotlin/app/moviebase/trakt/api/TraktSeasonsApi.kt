package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktSeason
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktSeasonsApi(
    private val client: HttpClient,
) {
    /**
     * Returns all seasons for a show including the number of episodes in each season.
     */
    suspend fun getSummary(
        showId: String,
        extended: TraktExtended? = null,
    ): List<TraktSeason> =
        client
            .get {
                endPoint("shows", showId, "seasons")
                extended?.let { parameterExtended(it) }
            }.body()

    /**
     * Returns all episodes for a specific season of a show.
     */
    suspend fun getSeason(
        showId: String,
        seasonNumber: Int,
        extended: TraktExtended? = null,
    ): List<TraktEpisode> =
        client
            .get {
                endPointSeasons(showId, seasonNumber)
                extended?.let { parameterExtended(it) }
            }.body()

    /**
     * Returns rating (between 0 and 10) and distribution for a season.
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     */
    suspend fun getRatings(
        showId: String,
        seasonNumber: Int,
    ): TraktRating =
        client
            .get {
                endPointSeasons(showId, seasonNumber, "ratings")
            }.body()

    /**
     * Returns stats (watchers, plays, collectors, etc.) for a season.
     */
    suspend fun getStats(
        showId: String,
        seasonNumber: Int,
    ): TraktRating =
        client
            .get {
                endPointSeasons(showId, seasonNumber, "stats")
            }.body()

    /**
     * Returns translation data for a season.
     */
    suspend fun getTranslations(
        showId: String,
        seasonNumber: Int,
        language: String? = null,
    ): List<TraktSeason> =
        client
            .get {
                endPointSeasons(showId, seasonNumber, "translations", language ?: "")
            }.body()

    /**
     * Returns all top level comments for a season.
     */
    suspend fun getComments(
        showId: String,
        seasonNumber: Int,
        sort: String = "newest",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktSeason> =
        client
            .get {
                endPointSeasons(showId, seasonNumber, "comments", sort)
                parameterPage(page)
                parameterLimit(limit)
            }.body()

    /**
     * Path: /shows/id/seasons/season/...
     */
    private fun HttpRequestBuilder.endPointSeasons(
        showId: String,
        seasonNumber: Int,
        vararg paths: String,
    ) {
        endPoint("shows", showId, "seasons", seasonNumber.toString(), *paths)
    }
}
