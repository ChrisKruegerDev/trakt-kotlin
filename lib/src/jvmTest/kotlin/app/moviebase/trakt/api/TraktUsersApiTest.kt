package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktUsersApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "users/requests" to "users/follow_requests.json",
                    "users/likes?page=1&limit=10" to "users/likes.json",
                    "users/hidden/recommendations?page=1&limit=10" to "users/hidden_items.json",
                ),
        )

    val classToTest = TraktUsersApi(client)

    @Test
    fun `it can fetch follower requests`() =
        runTest {
            val requests = classToTest.getFollowerRequests()

            assertThat(requests).isNotEmpty()
            val first = requests.first()
            assertThat(first.id).isEqualTo(12345)
            assertThat(first.user.userName).isEqualTo("johndoe")
        }

    @Test
    fun `it can fetch likes`() =
        runTest {
            val likes = classToTest.getLikes()

            assertThat(likes).isNotEmpty()
            val first = likes.first()
            assertThat(first.type).isEqualTo("list")
            assertThat(first.list).isNotNull()
            assertThat(first.list?.name).isEqualTo("Best Sci-Fi Movies")
        }

    @Test
    fun `it can fetch hidden items`() =
        runTest {
            val hidden = classToTest.getHiddenItems("recommendations")

            assertThat(hidden).isNotEmpty()
            val first = hidden.first()
            assertThat(first.movie).isNotNull()
        }
}
