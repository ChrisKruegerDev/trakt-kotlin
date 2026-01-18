package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktScrobbleRequest
import app.moviebase.trakt.model.TraktScrobbleResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktScrobbleApi(
    private val client: HttpClient,
) {

    suspend fun startWatching(request: TraktScrobbleRequest): TraktScrobbleResponse = client.post {
        endPointScrobble("start")
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()

    suspend fun pauseWatching(request: TraktScrobbleRequest): TraktScrobbleResponse = client.post {
        endPointScrobble("pause")
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()

    suspend fun stopWatching(request: TraktScrobbleRequest): TraktScrobbleResponse = client.post {
        endPointScrobble("stop")
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()

    private fun HttpRequestBuilder.endPointScrobble(vararg paths: String) {
        endPoint("scrobble", *paths)
    }
}
