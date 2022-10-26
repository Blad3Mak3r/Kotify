package tv.blademaker.kotify.models

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tv.blademaker.kotify.serializers.ItemListSerializer

@Serializable
data class PlaylistPagination(
    override val href: String,
    @Serializable(with = ItemListSerializer::class)
    override val items: List<Item>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int? = null,
    override val previous: String? = null,
    override val total: Int
) : ItemPaginator<PlaylistPagination.Item> {
    override val nextValues: NextValues? = nextValues(this)

    @Serializable
    data class Item(
        @SerialName("added_at") val addedAt: String,
        @SerialName("added_by") val addedBy: User? = null,
        @SerialName("is_local") val isLocal: Boolean = false,
        @SerialName("primary_color") val primaryColor: String? = null,
        val track: Track
    )
}