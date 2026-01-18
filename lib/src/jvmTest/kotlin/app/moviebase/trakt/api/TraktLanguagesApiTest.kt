package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktLanguagesApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "languages/movies" to "languages/movies.json",
                    "languages/shows" to "languages/shows.json",
                ),
        )

    val classToTest = TraktLanguagesApi(client)

    @Test
    fun `it can fetch movie languages`() =
        runTest {
            val languages = classToTest.getMovieLanguages()

            assertThat(languages).isNotEmpty()
            val first = languages.first()
            assertThat(first.name).isEqualTo("English")
            assertThat(first.code).isEqualTo("en")
        }

    @Test
    fun `it can fetch show languages`() =
        runTest {
            val languages = classToTest.getShowLanguages()

            assertThat(languages).isNotEmpty()
            val first = languages.first()
            assertThat(first.name).isEqualTo("English")
            assertThat(first.code).isEqualTo("en")
        }
}
