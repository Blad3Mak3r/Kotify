package tv.blademaker.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val id: String,
    val name: String,
    val description: String? = null,
    val collaborative: Boolean,
    @SerialName("external_urls") val externalUrls: Map<String, String>,
    val followers: Followers? = null,
    val href: String,
    val images: List<Image>,
    val owner: Owner,
    val public: Boolean,
    val tracks: PlaylistPagination,
    val type: String,
    val uri: String
) {
    @Serializable
    data class Owner(
        val id: String,
        @SerialName("display_name") val displayName: String? = null,
        @SerialName("external_urls") val externalUrls: Map<String, String>,
        val followers: Followers? = null,
        val href: String,
        val images: List<Image>? = null,
        val type: String,
        val uri: String
    )
}

@Serializable
data class PartialPlaylist(
    val id: String,
    val name: String,
    val description: String? = null,
    val collaborative: Boolean,
    val href: String,
    val images: List<Image>,
    val public: Boolean,
    val type: String,
    val uri: String
)