package tv.blademaker.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tv.blademaker.kotify.serializers.ItemListSerializer
import java.net.URL

interface ItemPagination<T : Any> {
    val href: String
    val items: List<T>
    val limit: Int
    val next: String?
    val offset: Int?
    val previous: String?
    val total: Int

    val nextValues: NextValues?

    fun nextValues(pagination: ItemPagination<T>): NextValues? {
        val next = pagination.next ?: return null

        val url = URL(next)

        val query = url.query.split("[?&]".toRegex()).associate {
            val values = it.split("=")
            values[0] to values[1]
        }

        val offset = query["offset"]?.toIntOrNull() ?: return null
        val limit = query["limit"]?.toIntOrNull() ?: pagination.limit

        return NextValues(offset, limit)
    }
}

internal suspend fun <T : Any> paginatedRequest(limitPerPage: Int, cursor: Int, pages: Int, fn: suspend (limit: Int, offset: Int) -> ItemPagination<T>): List<T> {
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

@Serializable
data class AlbumPagination(
    override val href: String,
    override val items: List<Track>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int? = null,
    override val previous: String? = null,
    override val total: Int
) : ItemPagination<Track> {
    override val nextValues: NextValues? = nextValues(this)
}

@Serializable
data class AlbumsPagination(
    override val href: String,
    override val items: List<PartialAlbum>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int? = null,
    override val previous: String? = null,
    override val total: Int
) : ItemPagination<PartialAlbum> {
    override val nextValues: NextValues? = nextValues(this)
}

@Serializable
data class ArtistsPagination(
    override val href: String,
    override val items: List<Artist>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int? = null,
    override val previous: String? = null,
    override val total: Int
) : ItemPagination<Artist> {
    override val nextValues: NextValues? = nextValues(this)
}

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
) : ItemPagination<PlaylistPagination.Item> {
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

@Serializable
data class PlaylistsPagination(
    override val href: String,
    override val items: List<PartialPlaylist>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int? = null,
    override val previous: String? = null,
    override val total: Int
) : ItemPagination<PartialPlaylist> {
    override val nextValues: NextValues? = nextValues(this)
}

@Serializable
data class TracksPagination(
    override val href: String,
    override val items: List<Track>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int? = null,
    override val previous: String? = null,
    override val total: Int
) : ItemPagination<Track> {
    override val nextValues: NextValues? = nextValues(this)
}