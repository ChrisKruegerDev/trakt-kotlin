package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktLanguage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktLanguagesApi(
    private val client: HttpClient,
) {

    suspend fun getMovieLanguages(): List<TraktLanguage> = client.get {
        endPointLanguages("movies")
    }.body()

    suspend fun getShowLanguages(): List<TraktLanguage> = client.get {
        endPointLanguages("shows")
    }.body()

    private fun HttpRequestBuilder.endPointLanguages(vararg paths: String) {
        endPoint("languages", *paths)
    }
}
