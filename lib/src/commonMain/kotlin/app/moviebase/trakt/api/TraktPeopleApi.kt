package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktPerson
import app.moviebase.trakt.model.TraktPersonMovieCredits
import app.moviebase.trakt.model.TraktPersonShowCredits
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktPeopleApi(
    private val client: HttpClient,
) {
    /**
     * Returns a single person's details.
     *
     * @param personId trakt ID, trakt slug, or IMDB ID. Example: "bryan-cranston".
     */
    suspend fun getSummary(
        personId: String,
        extended: TraktExtended? = null,
    ): TraktPerson = client.get {
        endPointPeople(personId)
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Returns all movies where this person is in the cast or crew.
     *
     * @param personId trakt ID, trakt slug, or IMDB ID. Example: "bryan-cranston".
     */
    suspend fun getMovieCredits(
        personId: String,
        extended: TraktExtended? = null,
    ): TraktPersonMovieCredits = client.get {
        endPointPeople(personId, "movies")
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Returns all TV shows where this person is in the cast or crew.
     *
     * @param personId trakt ID, trakt slug, or IMDB ID. Example: "bryan-cranston".
     */
    suspend fun getShowCredits(
        personId: String,
        extended: TraktExtended? = null,
    ): TraktPersonShowCredits = client.get {
        endPointPeople(personId, "shows")
        extended?.let { parameterExtended(it) }
    }.body()

    /**
     * Returns all lists that contain this person.
     *
     * @param personId trakt ID, trakt slug, or IMDB ID. Example: "bryan-cranston".
     * @param listType Filter for a specific list type: all, personal, official, watchlist, favorites, or recommendations.
     * @param sort How to sort the results: popular, likes, comments, items, added, or updated.
     */
    suspend fun getLists(
        personId: String,
        listType: String = "all",
        sort: String = "popular",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktList> = client.get {
        endPointPeople(personId, "lists", listType, sort)
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    private fun HttpRequestBuilder.endPointPeople(
        personId: String,
        vararg paths: String,
    ) {
        endPoint("people", personId, *paths)
    }
}
