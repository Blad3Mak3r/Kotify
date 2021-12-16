package tv.blademaker.models

import kotlinx.coroutines.Deferred
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tv.blademaker.Kotify
import tv.blademaker.request.Request

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
    val tracks: TracksPaginator,
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