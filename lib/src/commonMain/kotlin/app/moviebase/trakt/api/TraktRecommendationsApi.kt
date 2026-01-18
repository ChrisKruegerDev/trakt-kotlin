package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktMovie
import app.moviebase.trakt.model.TraktShow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get

class TraktRecommendationsApi(
    private val client: HttpClient,
) {
    suspend fun getMovies(
        page: Int = 1,
        limit: Int = 10,
        extended: TraktExtended? = null,
    ): List<TraktMovie> = client.get {
        endPointRecommendations("movies")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun hideMovie(movieId: String) = client.delete {
        endPointRecommendations("movies", movieId)
    }

    suspend fun getShows(
        page: Int = 1,
        limit: Int = 10,
        extended: TraktExtended? = null,
    ): List<TraktShow> = client.get {
        endPointRecommendations("shows")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun hideShow(showId: String) = client.delete {
        endPointRecommendations("shows", showId)
    }

    private fun HttpRequestBuilder.endPointRecommendations(vararg paths: String) {
        endPoint("recommendations", *paths)
    }
}
