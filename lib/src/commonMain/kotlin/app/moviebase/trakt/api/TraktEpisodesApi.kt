package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktRating
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktEpisodesApi(
    private val client: HttpClient,
) {
    suspend fun getSummary(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        extended: TraktExtended = TraktExtended.FULL,
    ): TraktEpisode =
        client
            .get {
                endPointEpisodes(traktSlug, seasonNumber, episodeNumber)
                parameterExtended(extended)
            }.body()

    suspend fun getRating(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktRating =
        client
            .get {
                endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "ratings")
            }.body()

    suspend fun getStats(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktRating =
        client
            .get {
                endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "stats")
            }.body()

    suspend fun getTranslations(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        language: String? = null,
    ): List<TraktEpisode> =
        client
            .get {
                endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "translations", language ?: "")
            }.body()

    suspend fun getComments(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        sort: String = "newest",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktEpisode> =
        client
            .get {
                endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "comments", sort)
                parameterPage(page)
                parameterLimit(limit)
            }.body()

    private fun HttpRequestBuilder.endPointEpisodes(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        vararg paths: String,
    ) {
        endPoint(
            "shows",
            traktSlug,
            "seasons",
            seasonNumber.toString(),
            "episodes",
            episodeNumber.toString(),
            *paths,
        )
    }
}
