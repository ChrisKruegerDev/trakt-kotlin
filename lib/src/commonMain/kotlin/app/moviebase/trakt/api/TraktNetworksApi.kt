package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktNetwork
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktNetworksApi(
    private val client: HttpClient,
) {

    suspend fun getNetworks(): List<TraktNetwork> = client.get {
        endPointNetworks()
    }.body()

    private fun HttpRequestBuilder.endPointNetworks(vararg paths: String) {
        endPoint("networks", *paths)
    }
}
