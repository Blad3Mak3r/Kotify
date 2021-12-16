package tv.blademaker.models

import kotlinx.serialization.Serializable

@Serializable
data class Followers(
    val href: String? = null,
    val total: Int
)
