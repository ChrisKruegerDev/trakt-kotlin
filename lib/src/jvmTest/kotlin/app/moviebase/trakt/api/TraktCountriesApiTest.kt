package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktCountriesApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "countries/movies" to "countries/movies.json",
                    "countries/shows" to "countries/shows.json",
                ),
        )

    val classToTest = TraktCountriesApi(client)

    @Test
    fun `it can fetch movie countries`() =
        runTest {
            val countries = classToTest.getMovieCountries()

            assertThat(countries).isNotEmpty()
            val first = countries.first()
            assertThat(first.name).isEqualTo("United States")
            assertThat(first.code).isEqualTo("us")
        }

    @Test
    fun `it can fetch show countries`() =
        runTest {
            val countries = classToTest.getShowCountries()

            assertThat(countries).isNotEmpty()
            val first = countries.first()
            assertThat(first.name).isEqualTo("United States")
            assertThat(first.code).isEqualTo("us")
        }
}
