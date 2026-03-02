package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktNote
import app.moviebase.trakt.model.TraktNoteRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktNotesApi(
    private val client: HttpClient,
) {
    suspend fun addNote(request: TraktNoteRequest): TraktNote = client.post {
        endPoint("notes")
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()

    suspend fun getNote(id: Long): TraktNote = client.get {
        endPoint("notes", id.toString())
    }.body()

    suspend fun updateNote(id: Long, request: TraktNoteRequest): TraktNote = client.put {
        endPoint("notes", id.toString())
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()

    suspend fun deleteNote(id: Long) {
        client.delete {
            endPoint("notes", id.toString())
        }
    }

    suspend fun getItem(id: Long): TraktNote = client.get {
        endPoint("notes", id.toString(), "item")
    }.body()
}
