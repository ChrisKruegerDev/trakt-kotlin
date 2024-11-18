

@file:Suppress("ktlint:standard:filename")

package app.moviebase.trakt.model

import kotlinx.serialization.Serializable

@Serializable
data class TraktMovie(
    val runtime: Int? = null,
    val ids: TraktItemIds,
)
