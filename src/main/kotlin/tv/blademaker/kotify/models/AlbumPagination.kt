package tv.blademaker.kotify.models

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class AlbumPagination(
    val href: String,
    val items: List<Track>,
    val limit: Int,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int
) {
    val nextValues: NextValues? = nextValues(this)

    @Serializable
    data class NextValues(
        val offset: Int,
        val limit: Int
    )

    companion object {
        private fun nextValues(paginator: AlbumPagination): NextValues? {
            val next = paginator.next ?: return null

            val url = Url(next)

            val offset = url.parameters["offset"]?.toIntOrNull() ?: return null
            val limit = url.parameters["limit"]?.toIntOrNull() ?: paginator.limit

            return NextValues(offset, limit)
        }
    }
}