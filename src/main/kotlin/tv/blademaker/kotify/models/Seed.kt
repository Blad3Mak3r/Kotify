package tv.blademaker.kotify.models

@kotlinx.serialization.Serializable
data class Seed(
    val afterFilteringSize: Int,
    val afterRelinkingSize: Int,
    val href: String,
    val id: String,
    val initialPoolSize: Int,
    val type: String
)
