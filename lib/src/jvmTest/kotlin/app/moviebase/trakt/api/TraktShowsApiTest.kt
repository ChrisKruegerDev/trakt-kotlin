package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktShowsApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "shows/vikings?extended=full" to "shows/show_summary_vikings.json",
                    "shows/the-expanse/aliases" to "shows/aliases.json",
                    "shows/the-expanse/certifications" to "shows/certifications.json",
                    "shows/the-expanse/next_episode" to "shows/next_episode.json",
                    "shows/the-expanse/people" to "shows/people.json",
                    "shows/the-expanse/studios" to "shows/studios.json",
                ),
        )

    val classToTest = TraktShowsApi(client)

    @Test
    fun `it can fetch show summary`() =
        runTest {
            val traktShow = classToTest.getSummary("vikings", TraktExtended.FULL)

            assertThat(traktShow.title).isEqualTo("Vikings")
        }

    @Test
    fun `it can fetch show aliases`() =
        runTest {
            val aliases = classToTest.getAliases("the-expanse")

            assertThat(aliases).isNotEmpty()
            val first = aliases.first()
            assertThat(first.title).isEqualTo("The Expanse")
            assertThat(first.country).isEqualTo("us")
        }

    @Test
    fun `it can fetch show certifications`() =
        runTest {
            val certifications = classToTest.getCertifications("the-expanse")

            assertThat(certifications).isNotEmpty()
            val first = certifications.first()
            assertThat(first.name).isEqualTo("TV-14")
            assertThat(first.slug).isEqualTo("tv-14")
        }

    @Test
    fun `it can fetch next episode`() =
        runTest {
            val episode = classToTest.getNextEpisode("the-expanse")

            assertThat(episode).isNotNull()
            assertThat(episode?.season).isEqualTo(5)
            assertThat(episode?.number).isEqualTo(3)
            assertThat(episode?.title).isEqualTo("Mother")
        }

    @Test
    fun `it can fetch show people`() =
        runTest {
            val credits = classToTest.getPeople("the-expanse")

            assertThat(credits.cast).isNotEmpty()
            val firstCast = credits.cast.first()
            assertThat(firstCast.character).isEqualTo("James Holden")
            assertThat(firstCast.person.name).isEqualTo("Steven Strait")
        }

    @Test
    fun `it can fetch show studios`() =
        runTest {
            val studios = classToTest.getStudios("the-expanse")

            assertThat(studios).isNotEmpty()
            val first = studios.first()
            assertThat(first.name).isEqualTo("Alcon Entertainment")
        }
}
