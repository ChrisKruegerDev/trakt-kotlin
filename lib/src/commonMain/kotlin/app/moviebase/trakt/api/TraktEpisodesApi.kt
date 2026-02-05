package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktComment
import app.moviebase.trakt.model.TraktCommentSort
import app.moviebase.trakt.model.TraktCredits
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktStats
import app.moviebase.trakt.model.TraktTranslation
import app.moviebase.trakt.model.TraktUser
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
    ): TraktEpisode = client.get {
        endPointEpisodes(traktSlug, seasonNumber, episodeNumber)
        parameterExtended(extended)
    }.body()

    suspend fun getRating(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktRating = client.get {
        endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "ratings")
    }.body()

    suspend fun getStats(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktStats = client.get {
        endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "stats")
    }.body()

    suspend fun getTranslations(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        language: String? = null,
    ): List<TraktTranslation> = client.get {
        if (language != null) {
            endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "translations", language)
        } else {
            endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "translations")
        }
    }.body()

    suspend fun getComments(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        sort: TraktCommentSort = TraktCommentSort.NEWEST,
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktComment> = client.get {
        endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "comments", sort.value)
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getLists(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        type: String = "personal",
        sort: String = "popular",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktList> = client.get {
        endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "lists", type, sort)
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getPeople(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        extended: TraktExtended? = null,
    ): TraktCredits = client.get {
        endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "people")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getUsersWatching(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        extended: TraktExtended? = null,
    ): List<TraktUser> = client.get {
        endPointEpisodes(traktSlug, seasonNumber, episodeNumber, "watching")
        extended?.let { parameterExtended(it) }
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
