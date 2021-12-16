package tv.blademaker.models

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
)