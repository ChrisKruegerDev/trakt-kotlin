package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktCertifications
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktCertificationsApi(
    private val client: HttpClient,
) {

    suspend fun getMovieCertifications(): TraktCertifications = client.get {
        endPointCertifications("movies")
    }.body()

    suspend fun getShowCertifications(): TraktCertifications = client.get {
        endPointCertifications("shows")
    }.body()

    private fun HttpRequestBuilder.endPointCertifications(vararg paths: String) {
        endPoint("certifications", *paths)
    }
}
