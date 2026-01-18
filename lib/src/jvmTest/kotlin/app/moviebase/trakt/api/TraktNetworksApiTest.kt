package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktNetworksApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "networks" to "networks/list.json",
                ),
        )

    val classToTest = TraktNetworksApi(client)

    @Test
    fun `it can fetch networks`() =
        runTest {
            val networks = classToTest.getNetworks()

            assertThat(networks).isNotEmpty()
            val first = networks.first()
            assertThat(first.name).isEqualTo("Netflix")
            assertThat(first.ids?.trakt).isEqualTo(1)
            assertThat(first.ids?.slug).isEqualTo("netflix")
        }
}
