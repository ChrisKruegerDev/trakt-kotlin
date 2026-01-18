package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktComment
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktPopularList
import app.moviebase.trakt.model.TraktTrendingList
import app.moviebase.trakt.model.TraktUserListItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post

class TraktListsApi(
    private val client: HttpClient,
) {

    suspend fun getTrending(
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktTrendingList> = client.get {
        endPointLists("trending")
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getPopular(
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktPopularList> = client.get {
        endPointLists("popular")
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getList(
        listId: String,
        extended: TraktExtended? = null,
    ): TraktList = client.get {
        endPointLists(listId)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getListItems(
        listId: String,
        type: String? = null,
        page: Int = 1,
        limit: Int = 10,
        extended: TraktExtended? = null,
    ): List<TraktUserListItem> = client.get {
        if (type != null) {
            endPointLists(listId, "items", type)
        } else {
            endPointLists(listId, "items")
        }
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getListComments(
        listId: String,
        sort: String = "newest",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktComment> = client.get {
        endPointLists(listId, "comments", sort)
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun likeList(listId: String) = client.post {
        endPointLists(listId, "like")
    }

    suspend fun unlikeList(listId: String) = client.delete {
        endPointLists(listId, "like")
    }

    private fun HttpRequestBuilder.endPointLists(vararg paths: String) {
        endPoint("lists", *paths)
    }
}
