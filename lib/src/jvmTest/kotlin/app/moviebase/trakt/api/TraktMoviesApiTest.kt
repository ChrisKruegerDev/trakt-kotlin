package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktMoviesApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "movies/boxoffice" to "movies/boxoffice.json",
                    "movies/dune-part-two-2024/aliases" to "movies/aliases.json",
                    "movies/dune-part-two-2024/releases" to "movies/releases.json",
                    "movies/dune-part-two-2024/releases/us" to "movies/releases.json",
                    "movies/dune-part-two-2024/translations" to "movies/translations.json",
                    "movies/dune-part-two-2024/people" to "movies/people.json",
                    "movies/dune-part-two-2024/studios" to "movies/studios.json",
                ),
        )

    val classToTest = TraktMoviesApi(client)

    @Test
    fun `it can fetch box office movies`() =
        runTest {
            val movies = classToTest.getBoxOffice()

            assertThat(movies).isNotEmpty()
            val first = movies.first()
            assertThat(first.revenue).isEqualTo(150000000)
            assertThat(first.movie.title).isEqualTo("Dune: Part Two")
        }

    @Test
    fun `it can fetch movie aliases`() =
        runTest {
            val aliases = classToTest.getAliases("dune-part-two-2024")

            assertThat(aliases).isNotEmpty()
            val first = aliases.first()
            assertThat(first.title).isEqualTo("Dune 2")
            assertThat(first.country).isEqualTo("us")
        }

    @Test
    fun `it can fetch movie releases`() =
        runTest {
            val releases = classToTest.getReleases("dune-part-two-2024")

            assertThat(releases).isNotEmpty()
            val first = releases.first()
            assertThat(first.country).isEqualTo("us")
            assertThat(first.certification).isEqualTo("PG-13")
            assertThat(first.releaseType).isEqualTo("theatrical")
        }

    @Test
    fun `it can fetch movie releases for specific country`() =
        runTest {
            val releases = classToTest.getReleases("dune-part-two-2024", "us")

            assertThat(releases).isNotEmpty()
        }

    @Test
    fun `it can fetch movie translations`() =
        runTest {
            val translations = classToTest.getTranslations("dune-part-two-2024")

            assertThat(translations).isNotEmpty()
            val first = translations.first()
            assertThat(first.language).isEqualTo("en")
            assertThat(first.title).isEqualTo("Dune: Part Two")
        }

    @Test
    fun `it can fetch movie people`() =
        runTest {
            val credits = classToTest.getPeople("dune-part-two-2024")

            assertThat(credits.cast).isNotEmpty()
            val firstCast = credits.cast.first()
            assertThat(firstCast.character).isEqualTo("Paul Atreides")
            assertThat(firstCast.person.name).isEqualTo("Timothee Chalamet")

            assertThat(credits.crew?.directing).isNotEmpty()
        }

    @Test
    fun `it can fetch movie studios`() =
        runTest {
            val studios = classToTest.getStudios("dune-part-two-2024")

            assertThat(studios).isNotEmpty()
            val first = studios.first()
            assertThat(first.name).isEqualTo("Legendary Pictures")
            assertThat(first.country).isEqualTo("us")
        }
}
