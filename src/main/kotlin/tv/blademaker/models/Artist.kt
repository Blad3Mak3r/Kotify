package tv.blademaker.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("external_urls") val externalUrls: Map<String, String>,
    val followers: Followers? = null,
    val genres: List<String>? = null,
    val href: String,
    val id: String,
    val images: List<Image>? = null,
    val name: String,
    val popularity: Int? = null,
    val type: String,
    val uri: String
) {

    @Serializable
    data class Followers(
        val href: String,
        val total: Int
    )
}
