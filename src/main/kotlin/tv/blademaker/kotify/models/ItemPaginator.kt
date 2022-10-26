package tv.blademaker.kotify.models

import java.net.URL

interface ItemPaginator<T : Any> {
    val href: String
    val items: List<T>
    val limit: Int
    val next: String?
    val offset: Int?
    val previous: String?
    val total: Int

    val nextValues: NextValues?

    fun nextValues(paginator: ItemPaginator<T>): NextValues? {
        val next = paginator.next ?: return null

        val url = URL(next)

        val query = url.query.split("[?&]".toRegex()).associate {
            val values = it.split("=")
            values[0] to values[1]
        }

        val offset = query["offset"]?.toIntOrNull() ?: return null
        val limit = query["limit"]?.toIntOrNull() ?: paginator.limit

        return NextValues(offset, limit)
    }
}

internal suspend fun <T : Any> paginatedRequest(limitPerPage: Int, cursor: Int, pages: Int, fn: suspend (limit: Int, offset: Int) -> ItemPaginator<T>): List<T> {
    val items = mutableListOf<T>()

    var page = fn(limitPerPage, cursor)
    var next = page.nextValues
    var currentPage = 1

    items.addAll(page.items)

    while (next != null && currentPage <= pages) {
        currentPage++
        page = fn(limitPerPage, next.offset)
        next = page.nextValues
        items.addAll(page.items)
    }

    return items
}