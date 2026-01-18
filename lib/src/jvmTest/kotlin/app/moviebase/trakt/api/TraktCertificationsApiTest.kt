package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktCertificationsApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "certifications/movies" to "certifications/movies.json",
                    "certifications/shows" to "certifications/shows.json",
                ),
        )

    val classToTest = TraktCertificationsApi(client)

    @Test
    fun `it can fetch movie certifications`() =
        runTest {
            val certifications = classToTest.getMovieCertifications()

            assertThat(certifications.us).isNotEmpty()
            val first = certifications.us.first()
            assertThat(first.name).isEqualTo("G")
            assertThat(first.slug).isEqualTo("g")
            assertThat(first.description).isNotNull()
        }

    @Test
    fun `it can fetch show certifications`() =
        runTest {
            val certifications = classToTest.getShowCertifications()

            assertThat(certifications.us).isNotEmpty()
            val first = certifications.us.first()
            assertThat(first.name).isEqualTo("TV-Y")
            assertThat(first.slug).isEqualTo("tv-y")
        }
}
