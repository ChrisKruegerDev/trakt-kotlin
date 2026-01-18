package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktCountry
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktCountriesApi(
    private val client: HttpClient,
) {

    suspend fun getMovieCountries(): List<TraktCountry> = client.get {
        endPointCountries("movies")
    }.body()

    suspend fun getShowCountries(): List<TraktCountry> = client.get {
        endPointCountries("shows")
    }.body()

    private fun HttpRequestBuilder.endPointCountries(vararg paths: String) {
        endPoint("countries", *paths)
    }
}
