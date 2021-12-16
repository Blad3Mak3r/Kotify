package tv.blademaker.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: String,
    val name: String,
    @SerialName("external_urls") val externalUrls: Map<String, String>,
    val followers: Followers? = null,
    val genres: List<String>? = null,
    val href: String,
    val images: List<Image>? = null,
    val popularity: Int? = null,
    val type: String,
    val uri: String
)
