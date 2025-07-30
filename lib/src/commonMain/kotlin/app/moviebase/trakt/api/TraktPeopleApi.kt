package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktPerson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktPeopleApi(
    private val client: HttpClient,
) {
    suspend fun getSummary(
        personId: String,
        extended: TraktExtended? = null,
    ): TraktPerson =
        client
            .get {
                endPointPeople(personId)
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getMovieCredits(
        personId: String,
        extended: TraktExtended? = null,
    ): List<TraktPerson> =
        client
            .get {
                endPointPeople(personId, "movies")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getShowCredits(
        personId: String,
        extended: TraktExtended? = null,
    ): List<TraktPerson> =
        client
            .get {
                endPointPeople(personId, "shows")
                extended?.let { parameterExtended(it) }
            }.body()

    suspend fun getLists(
        personId: String,
        listType: String = "all",
        sort: String = "popular",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktPerson> =
        client
            .get {
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
