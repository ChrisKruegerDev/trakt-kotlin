package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktScrobbleRequest(
    @SerialName("movie") val movie: TraktScrobbleMovie? = null,
    @SerialName("episode") val episode: TraktScrobbleEpisode? = null,
    @SerialName("show") val show: TraktScrobbleShow? = null,
    @SerialName("progress") val progress: Float,
    @SerialName("app_version") val appVersion: String? = null,
    @SerialName("app_date") val appDate: String? = null,
)

@Serializable
data class TraktScrobbleMovie(
    @SerialName("title") val title: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("ids") val ids: TraktItemIds,
)

@Serializable
data class TraktScrobbleShow(
    @SerialName("title") val title: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("ids") val ids: TraktItemIds,
)

@Serializable
data class TraktScrobbleEpisode(
    @SerialName("season") val season: Int? = null,
    @SerialName("number") val number: Int? = null,
    @SerialName("ids") val ids: TraktItemIds? = null,
)

@Serializable
data class TraktScrobbleResponse(
    @SerialName("id") val id: Long,
    @SerialName("action") val action: TraktScrobbleAction,
    @SerialName("progress") val progress: Float,
    @SerialName("sharing") val sharing: TraktSharing? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("show") val show: TraktShow? = null,
)

@Serializable
enum class TraktScrobbleAction {
    @SerialName("start")
    START,

    @SerialName("pause")
    PAUSE,

    @SerialName("stop")
    STOP,

    @SerialName("scrobble")
    SCROBBLE,
}
