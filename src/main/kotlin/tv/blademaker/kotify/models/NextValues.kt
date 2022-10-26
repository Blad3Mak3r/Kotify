package tv.blademaker.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class NextValues(
    val offset: Int,
    val limit: Int
)
