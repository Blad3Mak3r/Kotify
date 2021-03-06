package tv.blademaker.kotify.models

import io.ktor.http.*
import kotlinx.serialization.*
import tv.blademaker.kotify.serializers.ItemListSerializer

@Serializable
data class PlaylistPagination(
    val href: String,
    @Serializable(with = ItemListSerializer::class)
    val items: List<Item>,
    val limit: Int,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int
) {
    val nextValues: NextValues? = nextValues(this)

    @Serializable
    data class Item(
        @SerialName("added_at") val addedAt: String,
        @SerialName("added_by") val addedBy: User? = null,
        @SerialName("is_local") val isLocal: Boolean = false,
        @SerialName("primary_color") val primaryColor: String? = null,
        val track: Track
    )

    @Serializable
    data class NextValues(
        val offset: Int,
        val limit: Int
    )

    companion object {
        private fun nextValues(paginator: PlaylistPagination): NextValues? {
            val next = paginator.next ?: return null

            val url = Url(next)

            val offset = url.parameters["offset"]?.toIntOrNull() ?: return null
            val limit = url.parameters["limit"]?.toIntOrNull() ?: paginator.limit

            return NextValues(offset, limit)
        }
    }
}