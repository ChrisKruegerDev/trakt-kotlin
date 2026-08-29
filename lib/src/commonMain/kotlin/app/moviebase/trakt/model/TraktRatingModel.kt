package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktRating(
    @SerialName("rating") val rating: Double = 0.0,
    @SerialName("votes") val votes: Int = 0,
    @SerialName("distribution") val distribution: TraktRatingDistribution? = null,
    @SerialName("trakt") val trakt: TraktOwnRating? = null,
    @SerialName("tmdb") val tmdb: TraktSourceRating? = null,
    @SerialName("imdb") val imdb: TraktSourceRating? = null,
    @SerialName("metascore") val metascore: TraktMetascoreRating? = null,
    @SerialName("rotten_tomatoes") val rottenTomatoes: TraktCriticRating? = null,
    @SerialName("letterboxd") val letterboxd: TraktSourceRating? = null,
    @SerialName("mal") val mal: TraktSourceRating? = null,
) {
    /** Trakt's own rating, which `extended=all` moves into [trakt] and omits from [rating]. */
    val resolvedRating: Double get() = trakt?.rating ?: rating

    /** Trakt's own vote count, which `extended=all` moves into [trakt] and omits from [votes]. */
    val resolvedVotes: Int get() = trakt?.votes ?: votes

    val resolvedDistribution: TraktRatingDistribution? get() = trakt?.distribution ?: distribution
}

@Serializable
data class TraktOwnRating(
    @SerialName("rating") val rating: Double? = null,
    @SerialName("votes") val votes: Int? = null,
    @SerialName("distribution") val distribution: TraktRatingDistribution? = null,
)

@Serializable
data class TraktSourceRating(
    @SerialName("rating") val rating: Float? = null,
    @SerialName("votes") val votes: Int? = null,
    @SerialName("link") val link: String? = null,
)

@Serializable
data class TraktMetascoreRating(
    @SerialName("rating") val rating: Int? = null,
    @SerialName("votes") val votes: Int? = null,
    @SerialName("link") val link: String? = null,
)

@Serializable
data class TraktCriticRating(
    @SerialName("rating") val rating: Int? = null,
    @SerialName("votes") val votes: Int? = null,
    @SerialName("link") val link: String? = null,
    @SerialName("user_rating") val userRating: Int? = null,
    @SerialName("state") val state: String? = null,
    @SerialName("user_state") val userState: String? = null,
)

@Serializable
data class TraktRatingDistribution(
    @SerialName("1") val rating1: Int = 0,
    @SerialName("2") val rating2: Int = 0,
    @SerialName("3") val rating3: Int = 0,
    @SerialName("4") val rating4: Int = 0,
    @SerialName("5") val rating5: Int = 0,
    @SerialName("6") val rating6: Int = 0,
    @SerialName("7") val rating7: Int = 0,
    @SerialName("8") val rating8: Int = 0,
    @SerialName("9") val rating9: Int = 0,
    @SerialName("10") val rating10: Int = 0,
)

/**
 * Statistics for a movie, show, season, or episode.
 */
@Serializable
data class TraktStats(
    @SerialName("watchers") val watchers: Int = 0,
    @SerialName("plays") val plays: Int = 0,
    @SerialName("collectors") val collectors: Int = 0,
    @SerialName("collected_episodes") val collectedEpisodes: Int = 0,
    @SerialName("comments") val comments: Int = 0,
    @SerialName("lists") val lists: Int = 0,
    @SerialName("votes") val votes: Int = 0,
    @SerialName("favorited") val favorited: Int = 0,
)
