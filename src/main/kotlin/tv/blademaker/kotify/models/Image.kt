package tv.blademaker.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val url: String,
    val height: Int? = null,
    val width: Int? = null
)
