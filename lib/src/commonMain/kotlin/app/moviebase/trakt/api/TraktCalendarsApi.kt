package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktCalendarMovie
import app.moviebase.trakt.model.TraktCalendarShow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktCalendarsApi(
    private val client: HttpClient,
) {

    // OAuth required - My Calendar endpoints

    suspend fun getMyShows(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointMyCalendar("shows", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getMyNewShows(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointMyCalendar("shows", "new", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getMySeasonPremieres(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointMyCalendar("shows", "premieres", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getMyFinales(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointMyCalendar("shows", "finales", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getMyMovies(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarMovie> = client.get {
        endPointMyCalendar("movies", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getMyDvd(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarMovie> = client.get {
        endPointMyCalendar("dvd", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getMyStreaming(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarMovie> = client.get {
        endPointMyCalendar("movies", "streaming", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    // Public endpoints - All Calendar

    suspend fun getAllShows(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointAllCalendar("shows", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAllNewShows(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointAllCalendar("shows", "new", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAllSeasonPremieres(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointAllCalendar("shows", "premieres", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAllFinales(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarShow> = client.get {
        endPointAllCalendar("shows", "finales", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAllMovies(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarMovie> = client.get {
        endPointAllCalendar("movies", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAllDvd(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarMovie> = client.get {
        endPointAllCalendar("dvd", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAllStreaming(
        startDate: String? = null,
        days: Int? = null,
        extended: TraktExtended? = null,
    ): List<TraktCalendarMovie> = client.get {
        endPointAllCalendar("movies", "streaming", startDate, days)
        extended?.let { parameterExtended(it) }
    }.body()

    private fun HttpRequestBuilder.endPointMyCalendar(
        type: String,
        startDate: String? = null,
        days: Int? = null,
    ) {
        val paths = buildList {
            add("calendars")
            add("my")
            add(type)
            startDate?.let { add(it) }
            days?.let { add(it.toString()) }
        }
        endPoint(*paths.toTypedArray())
    }

    private fun HttpRequestBuilder.endPointMyCalendar(
        type: String,
        subtype: String,
        startDate: String? = null,
        days: Int? = null,
    ) {
        val paths = buildList {
            add("calendars")
            add("my")
            add(type)
            add(subtype)
            startDate?.let { add(it) }
            days?.let { add(it.toString()) }
        }
        endPoint(*paths.toTypedArray())
    }

    private fun HttpRequestBuilder.endPointAllCalendar(
        type: String,
        startDate: String? = null,
        days: Int? = null,
    ) {
        val paths = buildList {
            add("calendars")
            add("all")
            add(type)
            startDate?.let { add(it) }
            days?.let { add(it.toString()) }
        }
        endPoint(*paths.toTypedArray())
    }

    private fun HttpRequestBuilder.endPointAllCalendar(
        type: String,
        subtype: String,
        startDate: String? = null,
        days: Int? = null,
    ) {
        val paths = buildList {
            add("calendars")
            add("all")
            add(type)
            add(subtype)
            startDate?.let { add(it) }
            days?.let { add(it.toString()) }
        }
        endPoint(*paths.toTypedArray())
    }
}
