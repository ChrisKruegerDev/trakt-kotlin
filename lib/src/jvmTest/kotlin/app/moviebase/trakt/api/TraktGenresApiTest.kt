package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktGenresApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "genres/movies" to "genres/movies.json",
                    "genres/shows" to "genres/shows.json",
                ),
        )

    val classToTest = TraktGenresApi(client)

    @Test
    fun `it can fetch movie genres`() =
        runTest {
            val genres = classToTest.getMovieGenres()

            assertThat(genres).isNotEmpty()
            val first = genres.first()
            assertThat(first.name).isEqualTo("Action")
            assertThat(first.slug).isEqualTo("action")
        }

    @Test
    fun `it can fetch show genres`() =
        runTest {
            val genres = classToTest.getShowGenres()

            assertThat(genres).isNotEmpty()
            val first = genres.first()
            assertThat(first.name).isEqualTo("Action")
            assertThat(first.slug).isEqualTo("action")
        }
}
