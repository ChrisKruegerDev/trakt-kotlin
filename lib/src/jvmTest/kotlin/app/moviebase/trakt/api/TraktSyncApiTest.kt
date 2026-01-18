package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktMediaType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktSyncApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "sync/last_activities" to "sync/last_activities.json",
                    "sync/playback" to "sync/playback.json",
                    "sync/playback/movie" to "sync/playback.json",
                ),
        )

    val classToTest = TraktSyncApi(client)

    @Test
    fun `it can fetch last activities`() =
        runTest {
            val activities = classToTest.getLastActivities()

            assertThat(activities.all).isNotNull()
            assertThat(activities.movies).isNotNull()
            assertThat(activities.movies?.watchedAt).isNotNull()
            assertThat(activities.episodes).isNotNull()
            assertThat(activities.shows).isNotNull()
        }

    @Test
    fun `it can fetch playback progress`() =
        runTest {
            val playback = classToTest.getPlaybackProgress()

            assertThat(playback).isNotEmpty()
            val first = playback.first()
            assertThat(first.id).isEqualTo(12345)
            assertThat(first.progress).isEqualTo(45.5f)
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
        }

    @Test
    fun `it can fetch playback progress for movies only`() =
        runTest {
            val playback = classToTest.getPlaybackProgress(type = TraktMediaType.MOVIE)

            assertThat(playback).isNotEmpty()
        }
}
