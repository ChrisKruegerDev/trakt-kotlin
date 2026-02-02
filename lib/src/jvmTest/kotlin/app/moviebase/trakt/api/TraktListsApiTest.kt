package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktListItemType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktListsApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "lists/trending?page=1&limit=10" to "lists/trending.json",
                    "lists/popular?page=1&limit=10" to "lists/popular.json",
                    "lists/best-sci-fi-movies" to "lists/list.json",
                    "lists/best-sci-fi-movies/items?page=1&limit=10" to "lists/items.json",
                ),
        )

    val classToTest = TraktListsApi(client)

    @Test
    fun `it can fetch trending lists`() =
        runTest {
            val lists = classToTest.getTrending()

            assertThat(lists).isNotEmpty()
            val first = lists.first()
            assertThat(first.likeCount).isEqualTo(125)
            assertThat(first.commentCount).isEqualTo(15)
            assertThat(first.list?.name).isEqualTo("Best Sci-Fi Movies")
        }

    @Test
    fun `it can fetch popular lists`() =
        runTest {
            val lists = classToTest.getPopular()

            assertThat(lists).isNotEmpty()
            val first = lists.first()
            assertThat(first.likeCount).isEqualTo(500)
            assertThat(first.list?.name).isEqualTo("Marvel Cinematic Universe")
        }

    @Test
    fun `it can fetch a single list`() =
        runTest {
            val list = classToTest.getList("best-sci-fi-movies")

            assertThat(list.name).isEqualTo("Best Sci-Fi Movies")
            assertThat(list.ids?.slug).isEqualTo("best-sci-fi-movies")
        }

    @Test
    fun `it can fetch list items`() =
        runTest {
            val items = classToTest.getListItems("best-sci-fi-movies")

            assertThat(items).isNotEmpty()
            val first = items.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.type).isEqualTo(TraktListItemType.MOVIE)
            assertThat(first.movie).isNotNull()
        }
}
