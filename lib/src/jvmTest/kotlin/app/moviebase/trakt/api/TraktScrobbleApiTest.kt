package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktItemIds
import app.moviebase.trakt.model.TraktScrobbleAction
import app.moviebase.trakt.model.TraktScrobbleEpisode
import app.moviebase.trakt.model.TraktScrobbleMovie
import app.moviebase.trakt.model.TraktScrobbleRequest
import app.moviebase.trakt.model.TraktScrobbleShow
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktScrobbleApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "scrobble/start" to "scrobble/start_movie.json",
                    "scrobble/pause" to "scrobble/pause_movie.json",
                    "scrobble/stop" to "scrobble/stop_episode.json",
                ),
        )

    val classToTest = TraktScrobbleApi(client)

    @Test
    fun `it can start watching a movie`() =
        runTest {
            val request = TraktScrobbleRequest(
                movie = TraktScrobbleMovie(
                    ids = TraktItemIds(trakt = 546896),
                ),
                progress = 25.5f,
            )

            val response = classToTest.startWatching(request)

            assertThat(response.id).isEqualTo(12345)
            assertThat(response.action).isEqualTo(TraktScrobbleAction.START)
            assertThat(response.progress).isEqualTo(25.5f)
            assertThat(response.movie).isNotNull()
        }

    @Test
    fun `it can pause watching a movie`() =
        runTest {
            val request = TraktScrobbleRequest(
                movie = TraktScrobbleMovie(
                    ids = TraktItemIds(trakt = 546896),
                ),
                progress = 50.0f,
            )

            val response = classToTest.pauseWatching(request)

            assertThat(response.id).isEqualTo(12345)
            assertThat(response.action).isEqualTo(TraktScrobbleAction.PAUSE)
            assertThat(response.progress).isEqualTo(50.0f)
        }

    @Test
    fun `it can stop watching an episode`() =
        runTest {
            val request = TraktScrobbleRequest(
                show = TraktScrobbleShow(
                    ids = TraktItemIds(trakt = 77199),
                ),
                episode = TraktScrobbleEpisode(
                    season = 4,
                    number = 1,
                ),
                progress = 95.0f,
            )

            val response = classToTest.stopWatching(request)

            assertThat(response.id).isEqualTo(67890)
            assertThat(response.action).isEqualTo(TraktScrobbleAction.SCROBBLE)
            assertThat(response.progress).isEqualTo(95.0f)
            assertThat(response.episode).isNotNull()
            assertThat(response.show).isNotNull()
        }
}
