package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktHiddenSection
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktUserSlug
import app.moviebase.trakt.model.TraktWatching
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
                    "users/me/ratings/movies?page=1&limit=10" to "users/ratings_movies.json",
                    "users/me/ratings/shows?page=1&limit=10" to "users/ratings_shows.json",
                    "users/me/watchlist/movies?page=1&limit=10" to "users/watchlist_movies.json",
                    "users/me/watchlist/shows?page=1&limit=10" to "users/watchlist_shows.json",
                    "users/me/favorites/movies?page=1&limit=10" to "users/favorites_movies.json",
                    "users/me/favorites/shows?page=1&limit=10" to "users/favorites_shows.json",
                    "users/me/watching" to "users/watching_movie.json",
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
            val likes = classToTest.getLikes(page = 1, limit = 10)

            assertThat(likes).isNotEmpty()
            val first = likes.first()
            assertThat(first.type).isEqualTo("list")
            assertThat(first.list).isNotNull()
            assertThat(first.list?.name).isEqualTo("Best Sci-Fi Movies")
        }

    @Test
    fun `it can fetch hidden items`() =
        runTest {
            val hidden = classToTest.getHiddenItems(TraktHiddenSection.RECOMMENDATIONS, page = 1, limit = 10)

            assertThat(hidden).isNotEmpty()
            val first = hidden.first()
            assertThat(first.movie).isNotNull()
        }

    @Test
    fun `it can fetch ratings for movies`() =
        runTest {
            val ratings = classToTest.getRatingsMovies(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(ratings).hasSize(2)
            val first = ratings.first()
            assertThat(first.rating).isEqualTo(8)
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
            assertThat(first.movie?.title).isEqualTo("Inception")
            assertThat(first.movie?.year).isEqualTo(2010)
            assertThat(first.movie?.ids?.trakt).isEqualTo(16662)
            assertThat(first.movie?.ids?.imdb).isEqualTo("tt1375666")

            val second = ratings[1]
            assertThat(second.rating).isEqualTo(10)
            assertThat(second.movie?.title).isEqualTo("The Dark Knight")
        }

    @Test
    fun `it can fetch ratings for shows`() =
        runTest {
            val ratings = classToTest.getRatingsShows(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(ratings).hasSize(1)
            val first = ratings.first()
            assertThat(first.rating).isEqualTo(9)
            assertThat(first.type).isEqualTo(TraktMediaType.SHOW)
            assertThat(first.show).isNotNull()
            assertThat(first.show?.title).isEqualTo("Breaking Bad")
            assertThat(first.show?.year).isEqualTo(2008)
            assertThat(first.show?.ids?.trakt).isEqualTo(1388)
        }

    @Test
    fun `it can fetch watchlist movies`() =
        runTest {
            val watchlist = classToTest.getWatchlistMovies(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(watchlist).hasSize(1)
            val first = watchlist.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.id).isEqualTo(12345678)
            assertThat(first.notes).isEqualTo("Must watch this weekend")
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
            assertThat(first.movie?.title).isEqualTo("Dune: Part Two")
            assertThat(first.movie?.year).isEqualTo(2024)
        }

    @Test
    fun `it can fetch watchlist shows`() =
        runTest {
            val watchlist = classToTest.getWatchlistShows(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(watchlist).hasSize(1)
            val first = watchlist.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.type).isEqualTo(TraktMediaType.SHOW)
            assertThat(first.show).isNotNull()
            assertThat(first.show?.title).isEqualTo("The Last of Us")
            assertThat(first.show?.year).isEqualTo(2023)
            assertThat(first.show?.ids?.trakt).isEqualTo(158947)
        }

    @Test
    fun `it can fetch favorite movies`() =
        runTest {
            val favorites = classToTest.getFavoriteMovies(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(favorites).hasSize(1)
            val first = favorites.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.id).isEqualTo(11111111)
            assertThat(first.notes).isEqualTo("All-time favorite")
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
            assertThat(first.movie?.title).isEqualTo("The Shawshank Redemption")
            assertThat(first.movie?.year).isEqualTo(1994)
        }

    @Test
    fun `it can fetch watching for current user`() =
        runTest {
            val watching = classToTest.getWatching()

            assertThat(watching).isNotNull()
            assertThat(watching!!.action).isEqualTo("watching")
            assertThat(watching.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(watching.movie).isNotNull()
            assertThat(watching.movie?.title).isEqualTo("Guardians of the Galaxy")
            assertThat(watching.movie?.year).isEqualTo(2014)
            assertThat(watching.movie?.ids?.trakt).isEqualTo(28)
            assertThat(watching.movie?.ids?.imdb).isEqualTo("tt2015381")
            assertThat(watching.expiresAt).isNotNull()
            assertThat(watching.startedAt).isNotNull()
        }

    @Test
    fun `it can fetch favorite shows`() =
        runTest {
            val favorites = classToTest.getFavoriteShows(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(favorites).hasSize(1)
            val first = favorites.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.notes).isEqualTo("Best TV show ever")
            assertThat(first.type).isEqualTo(TraktMediaType.SHOW)
            assertThat(first.show).isNotNull()
            assertThat(first.show?.title).isEqualTo("The Wire")
            assertThat(first.show?.year).isEqualTo(2002)
        }
}
