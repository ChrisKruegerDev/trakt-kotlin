package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktSearchResult
import app.moviebase.trakt.model.TraktSearchType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TraktSearchApi(private val client: HttpClient) {

    /**
     * Lookup items by their Trakt, IMDB, TMDB, TVDB, or TVRage ID.
     *
     * URL example: /search/tmdb/:id?type=movie
     *
     * @see [Search - ID Lookup](https://trakt.docs.apiary.io/#reference/search/id-lookup)
     */
    suspend fun searchIdLookup(searchType: TraktSearchType, id: String, mediaType: TraktMediaType): List<TraktSearchResult> = client.get {
        endPointSearch(searchType.value, id)
        parameter("type", mediaType.value)
    }.body()

    private fun HttpRequestBuilder.endPointSearch(vararg paths: String) {
        endPoint("search", *paths)
    }
}
