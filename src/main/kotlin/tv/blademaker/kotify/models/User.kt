package tv.blademaker.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    @SerialName("display_name") val displayName: String? = null,
    val emails: String? = null,
    @SerialName("external_urls") val externalUrls: Map<String, String>? = null,
    val uri: String,
    val href: String,
    val type: String
) {
    @Serializable
    data class TopTracks(
        val href: String,
        val items: List<Track>,
        val limit: Int,
        val next: String? = null,
        val offset: Int,
        val previous: String? = null,
        val total: Int
    )

    @Serializable
    data class TopArtists(
        val href: String,
        val items: List<Artist>,
        val limit: Int,
        val next: String? = null,
        val offset: Int,
        val previous: String? = null,
        val total: Int
    )
}