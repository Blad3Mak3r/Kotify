package tv.blademaker.models

import kotlinx.serialization.Serializable

@Serializable
data class TracksPaginator(
    val href: String,
    val items: List<Track>,
    val limit: Int,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int
) {
    val hasNext: Boolean
        get() = next != null
}