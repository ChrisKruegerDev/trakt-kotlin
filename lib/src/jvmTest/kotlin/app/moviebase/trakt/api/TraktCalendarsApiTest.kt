package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktCalendarsApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "calendars/my/shows" to "calendars/shows.json",
                    "calendars/my/shows/2024-01-15/7" to "calendars/shows.json",
                    "calendars/my/movies" to "calendars/movies.json",
                    "calendars/all/shows" to "calendars/shows.json",
                    "calendars/all/movies" to "calendars/movies.json",
                    "calendars/releases/hot" to "calendars/hot_releases.json",
                    "calendars/releases/hot/2026-08-09/7" to "calendars/hot_releases.json",
                    "calendars/releases/hot/finales/2026-08-09/7" to "calendars/hot_releases.json",
                    "calendars/releases/hot/new" to "calendars/hot_releases.json",
                    "calendars/releases/hot/premieres" to "calendars/hot_releases.json",
                ),
        )

    val classToTest = TraktCalendarsApi(client)

    @Test
    fun `it can fetch my shows calendar`() =
        runTest {
            val shows = classToTest.getMyShows()

            assertThat(shows).isNotEmpty()
            val first = shows.first()
            assertThat(first.firstAired).isNotNull()
            assertThat(first.episode).isNotNull()
            assertThat(first.episode?.title).isEqualTo("The Beginning")
            assertThat(first.show?.title).isEqualTo("The Expanse")
        }

    @Test
    fun `it can fetch my shows calendar with date range`() =
        runTest {
            val shows = classToTest.getMyShows(startDate = "2024-01-15", days = 7)

            assertThat(shows).isNotEmpty()
        }

    @Test
    fun `it can fetch my movies calendar`() =
        runTest {
            val movies = classToTest.getMyMovies()

            assertThat(movies).isNotEmpty()
            val first = movies.first()
            assertThat(first.released).isEqualTo("2024-01-20")
            assertThat(first.movie?.title).isEqualTo("Dune: Part Two")
        }

    @Test
    fun `it can fetch all shows calendar`() =
        runTest {
            val shows = classToTest.getAllShows()

            assertThat(shows).isNotEmpty()
            assertThat(shows.first().show?.title).isEqualTo("The Expanse")
        }

    @Test
    fun `it can fetch all movies calendar`() =
        runTest {
            val movies = classToTest.getAllMovies()

            assertThat(movies).isNotEmpty()
            assertThat(movies.first().movie?.title).isEqualTo("Dune: Part Two")
        }

    @Test
    fun `it can fetch hot releases`() =
        runTest {
            val releases = classToTest.getHotReleases()

            assertThat(releases).hasSize(2)
            val first = releases.first()
            assertThat(first.show?.title).isEqualTo("Silo")
            assertThat(first.episode?.season).isEqualTo(3)
            assertThat(first.released).isEqualTo("2026-08-09")
            assertThat(releases[1].released).isNull()
        }

    @Test
    fun `it can fetch hot releases for a date range`() =
        runTest {
            val releases = classToTest.getHotReleases(startDate = "2026-08-09", days = 7)

            assertThat(releases).hasSize(2)
            assertThat(releases.first().firstAired).isNotNull()
        }

    @Test
    fun `it can fetch hot new shows and premieres`() =
        runTest {
            assertThat(classToTest.getHotNewShows()).hasSize(2)
            assertThat(classToTest.getHotSeasonPremieres()).hasSize(2)
        }

    @Test
    fun `it can fetch hot finales for a date range`() =
        runTest {
            val finales = classToTest.getHotFinales(startDate = "2026-08-09", days = 7)

            assertThat(finales).hasSize(2)
            assertThat(finales.first().show?.ids?.tmdb).isEqualTo(125988)
        }
}
