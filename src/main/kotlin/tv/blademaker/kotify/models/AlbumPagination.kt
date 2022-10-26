package tv.blademaker.kotify.models

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class AlbumPagination(
    override val href: String,
    override val items: List<Track>,
    override val limit: Int,
    override val next: String? = null,
    override val offset: Int? = null,
    override val previous: String? = null,
    override val total: Int
) : ItemPaginator<Track> {
    override val nextValues: NextValues? = nextValues(this)
}