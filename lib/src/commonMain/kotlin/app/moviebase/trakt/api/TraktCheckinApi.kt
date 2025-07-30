package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktCheckin
import app.moviebase.trakt.model.TraktCheckinItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktCheckinApi(
    private val client: HttpClient,
) {
    suspend fun postCheckin(item: TraktCheckinItem): TraktCheckin.Active =
        client
            .post {
                endPoint("checkin")
                contentType(ContentType.Application.Json)
                setBody(item)
            }.body()

    suspend fun deleteCheckin(): TraktCheckin.Active =
        client
            .delete {
                endPoint("checkin")
            }.body()
}
