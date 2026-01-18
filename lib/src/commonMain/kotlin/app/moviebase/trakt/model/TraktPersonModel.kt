package app.moviebase.trakt.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktPerson(
    @SerialName("name") val name: String,
    @SerialName("ids") val ids: TraktPersonIds,
    @SerialName("biography") val biography: String? = null,
    @SerialName("birthday") val birthday: String? = null,
    @SerialName("death") val death: LocalDate? = null,
    @SerialName("birthplace") val birthplace: String? = null,
    @SerialName("homepage") val homepage: String? = null,
)

/**
 * Credits for a person in movies.
 */
@Serializable
data class TraktPersonMovieCredits(
    @SerialName("cast") val cast: List<TraktPersonMovieCastCredit> = emptyList(),
    @SerialName("crew") val crew: TraktPersonMovieCrewCredits? = null,
)

@Serializable
data class TraktPersonMovieCastCredit(
    @SerialName("character") val character: String? = null,
    @SerialName("characters") val characters: List<String> = emptyList(),
    @SerialName("movie") val movie: TraktMovie,
)

@Serializable
data class TraktPersonMovieCrewCredits(
    @SerialName("production") val production: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("art") val art: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("crew") val crew: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("costume & make-up") val costumeMakeUp: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("directing") val directing: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("writing") val writing: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("sound") val sound: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("camera") val camera: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("visual effects") val visualEffects: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("lighting") val lighting: List<TraktPersonMovieCrewCredit> = emptyList(),
    @SerialName("editing") val editing: List<TraktPersonMovieCrewCredit> = emptyList(),
)

@Serializable
data class TraktPersonMovieCrewCredit(
    @SerialName("job") val job: String? = null,
    @SerialName("jobs") val jobs: List<String> = emptyList(),
    @SerialName("movie") val movie: TraktMovie,
)

/**
 * Credits for a person in TV shows.
 */
@Serializable
data class TraktPersonShowCredits(
    @SerialName("cast") val cast: List<TraktPersonShowCastCredit> = emptyList(),
    @SerialName("crew") val crew: TraktPersonShowCrewCredits? = null,
)

@Serializable
data class TraktPersonShowCastCredit(
    @SerialName("character") val character: String? = null,
    @SerialName("characters") val characters: List<String> = emptyList(),
    @SerialName("episode_count") val episodeCount: Int? = null,
    @SerialName("series_regular") val seriesRegular: Boolean? = null,
    @SerialName("show") val show: TraktShow,
)

@Serializable
data class TraktPersonShowCrewCredits(
    @SerialName("production") val production: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("art") val art: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("crew") val crew: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("costume & make-up") val costumeMakeUp: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("directing") val directing: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("writing") val writing: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("sound") val sound: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("camera") val camera: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("visual effects") val visualEffects: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("lighting") val lighting: List<TraktPersonShowCrewCredit> = emptyList(),
    @SerialName("editing") val editing: List<TraktPersonShowCrewCredit> = emptyList(),
)

@Serializable
data class TraktPersonShowCrewCredit(
    @SerialName("job") val job: String? = null,
    @SerialName("jobs") val jobs: List<String> = emptyList(),
    @SerialName("episode_count") val episodeCount: Int? = null,
    @SerialName("show") val show: TraktShow,
)
