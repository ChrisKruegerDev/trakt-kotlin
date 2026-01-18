package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktCertification(
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("description") val description: String? = null,
)

@Serializable
data class TraktCertifications(
    @SerialName("us") val us: List<TraktCertification> = emptyList(),
)

@Serializable
data class TraktGenre(
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
)

@Serializable
data class TraktNetwork(
    @SerialName("name") val name: String,
    @SerialName("ids") val ids: TraktNetworkIds? = null,
)

@Serializable
data class TraktNetworkIds(
    @SerialName("trakt") val trakt: Int? = null,
    @SerialName("slug") val slug: String? = null,
    @SerialName("tmdb") val tmdb: Int? = null,
)

@Serializable
data class TraktCountry(
    @SerialName("name") val name: String,
    @SerialName("code") val code: String,
)

@Serializable
data class TraktLanguage(
    @SerialName("name") val name: String,
    @SerialName("code") val code: String,
)
