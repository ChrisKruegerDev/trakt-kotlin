package app.moviebase.trakt.model

import kotlinx.serialization.Serializable

@Serializable
data class TraktMovie(
    val runtime: Int? = null,
    val ids: TraktItemIds,
)

@Serializable
data class TraktTrendingMovie(
    val watchers: Int,
    val movie: TraktMovie,
)

@Serializable
data class TraktAnticipatedMovie(
    val listCount: Int? = null,
    val movie: TraktMovie,
)
