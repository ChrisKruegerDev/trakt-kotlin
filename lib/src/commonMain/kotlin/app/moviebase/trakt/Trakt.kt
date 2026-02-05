package app.moviebase.trakt

import app.moviebase.trakt.api.TraktAuthApi
import app.moviebase.trakt.api.TraktCalendarsApi
import app.moviebase.trakt.api.TraktCertificationsApi
import app.moviebase.trakt.api.TraktCheckinApi
import app.moviebase.trakt.api.TraktCommentsApi
import app.moviebase.trakt.api.TraktCountriesApi
import app.moviebase.trakt.api.TraktEpisodesApi
import app.moviebase.trakt.api.TraktGenresApi
import app.moviebase.trakt.api.TraktLanguagesApi
import app.moviebase.trakt.api.TraktListsApi
import app.moviebase.trakt.api.TraktMoviesApi
import app.moviebase.trakt.api.TraktNetworksApi
import app.moviebase.trakt.api.TraktPeopleApi
import app.moviebase.trakt.api.TraktRecommendationsApi
import app.moviebase.trakt.api.TraktScrobbleApi
import app.moviebase.trakt.api.TraktSearchApi
import app.moviebase.trakt.api.TraktSeasonsApi
import app.moviebase.trakt.api.TraktShowsApi
import app.moviebase.trakt.api.TraktSyncApi
import app.moviebase.trakt.api.TraktUsersApi
import app.moviebase.trakt.core.HttpClientFactory
import app.moviebase.trakt.core.TraktDsl
import app.moviebase.trakt.core.interceptRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header

@TraktDsl
fun Trakt(block: TraktClientConfig.() -> Unit): Trakt {
    val config = TraktClientConfig().apply(block)
    return Trakt(config)
}

class Trakt internal constructor(
    private val config: TraktClientConfig,
) {
    constructor(traktApiKey: String) : this(TraktClientConfig.withKey(traktApiKey))

    val client: HttpClient by lazy {
        HttpClientFactory.create(config).apply {
            interceptRequest {
                it.header(TraktHeader.API_KEY, config.clientId)
                it.header(TraktHeader.API_VERSION, TraktWebConfig.VERSION)
            }
        }
    }

    init {
        requireNotNull(config.clientId) {
            "Trakt API key unavailable. Set the traktApiKey field in the class TraktClientConfig when instantiate the Trakt client."
        }
    }

    val auth by lazy { TraktAuthApi(client, config) }
    val movies by buildApi(::TraktMoviesApi)
    val shows by buildApi(::TraktShowsApi)
    val seasons by buildApi(::TraktSeasonsApi)
    val episodes by buildApi(::TraktEpisodesApi)
    val people by buildApi(::TraktPeopleApi)
    val checkin by buildApi(::TraktCheckinApi)
    val search by buildApi(::TraktSearchApi)
    val users by buildApi(::TraktUsersApi)
    val sync by buildApi(::TraktSyncApi)
    val recommendations by buildApi(::TraktRecommendationsApi)
    val comments by buildApi(::TraktCommentsApi)

    // New APIs
    val certifications by buildApi(::TraktCertificationsApi)
    val genres by buildApi(::TraktGenresApi)
    val networks by buildApi(::TraktNetworksApi)
    val countries by buildApi(::TraktCountriesApi)
    val languages by buildApi(::TraktLanguagesApi)
    val calendars by buildApi(::TraktCalendarsApi)
    val scrobble by buildApi(::TraktScrobbleApi)
    val lists by buildApi(::TraktListsApi)

    private inline fun <T> buildApi(crossinline builder: (HttpClient) -> T) =
        lazy {
            builder(client)
        }
}
