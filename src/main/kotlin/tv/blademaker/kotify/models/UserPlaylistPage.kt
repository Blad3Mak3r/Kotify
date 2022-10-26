package tv.blademaker.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class UserPlaylistsPage(
    override val href: String,
    override val items: List<PartialPlaylist>,
    override val limit: Int,
    override val next: String?,
    override val offset: Int?,
    override val previous: String?,
    override val total: Int
) : ItemPaginator<PartialPlaylist> {
    override val nextValues: NextValues? = nextValues(this)
}

@Serializable
data class UserAlbumsPage(
    override val href: String,
    override val items: List<Album>,
    override val limit: Int,
    override val next: String?,
    override val offset: Int?,
    override val previous: String?,
    override val total: Int
) : ItemPaginator<Album> {
    override val nextValues: NextValues? = nextValues(this)
}