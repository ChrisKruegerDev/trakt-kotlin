package app.moviebase.trakt.url

import app.moviebase.trakt.TraktUrlParameter
import app.moviebase.trakt.TraktWebConfig
import app.moviebase.trakt.model.TraktMediaType

object TraktUrlBuilder {

    /**
     * Example: https://trakt.tv/users/andrewbloom
     */
    fun buildUserPage(userId: String) = "${TraktWebConfig.WEBSITE_BASE_URL}/${TraktUrlParameter.USERS}/$userId"

    /**
     * Example: https://trakt.tv/comments/283816
     */
    fun buildCommentPage(commentId: Int) = "${TraktWebConfig.WEBSITE_BASE_URL}/${TraktUrlParameter.COMMENTS}/$commentId"

    /**
     * https://trakt.tv/search/imdb/tt0468569
     */
    fun buildMediaPage(
        mediaType: TraktMediaType,
        imdbOrTraktId: String,
        seasonNumber: Int? = null,
        episodeNumber: Int? = null
    ): String {
        if (imdbOrTraktId.startsWith("tt")) {
            return "${TraktWebConfig.WEBSITE_BASE_URL}/search/imdb/$imdbOrTraktId"
        } else {
            val mediaTypeParam = if (mediaType == TraktMediaType.MOVIE) {
                "movies"
            } else {
                "shows"
            }

            var path = "${TraktWebConfig.WEBSITE_BASE_URL}/$mediaTypeParam/$imdbOrTraktId"

            if (mediaType == TraktMediaType.SEASON) {
                path += "/seasons/${seasonNumber}"
            }

            if (mediaType == TraktMediaType.EPISODE) {
                path += "/episodes/${episodeNumber}"
            }

            return path
        }
    }
}
