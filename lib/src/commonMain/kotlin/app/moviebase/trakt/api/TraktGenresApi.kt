package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktGenre
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktGenresApi(
    private val client: HttpClient,
) {

    suspend fun getMovieGenres(): List<TraktGenre> = client.get {
        endPointGenres("movies")
    }.body()

    suspend fun getShowGenres(): List<TraktGenre> = client.get {
        endPointGenres("shows")
    }.body()

    private fun HttpRequestBuilder.endPointGenres(vararg paths: String) {
        endPoint("genres", *paths)
    }
}
