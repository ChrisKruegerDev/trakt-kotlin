package app.moviebase.trakt.core

import app.moviebase.trakt.TraktHeader
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

data class TraktPage<T>(
    val items: List<T>,
    val page: Int? = null,
    val pageCount: Int? = null,
    val itemCount: Int? = null,
)

internal suspend inline fun <reified T> HttpResponse.bodyPage(): TraktPage<T> =
    TraktPage(
        items = body(),
        page = headers[TraktHeader.PAGINATION_PAGE]?.toIntOrNull(),
        pageCount = headers[TraktHeader.PAGINATION_PAGE_COUNT]?.toIntOrNull(),
        itemCount = headers[TraktHeader.PAGINATION_ITEM_COUNT]?.toIntOrNull(),
    )
