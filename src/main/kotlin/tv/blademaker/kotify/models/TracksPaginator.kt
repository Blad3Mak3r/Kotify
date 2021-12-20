package tv.blademaker.kotify.models

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TracksPaginator(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String? = null,
    val offset: Int? = null,
    val previous: String? = null,
    val total: Int
) {
    val nextValues: NextValues? = nextValues(this)

    @Serializable
    data class Item(
        @SerialName("added_at") val addedAt: String,
        @SerialName("added_by") val addedBy: User,
        @SerialName("is_local") val isLocal: Boolean,
        @SerialName("primary_color") val primaryColor: String? = null,
        val track: Track
    )

    @Serializable
    data class NextValues(
        val offset: Int,
        val limit: Int
    )

    companion object {
        private fun nextValues(paginator: TracksPaginator): NextValues? {
            val next = paginator.next ?: return null

            val url = Url(next)

            val offset = url.parameters["offset"]?.toIntOrNull() ?: return null
            val limit = url.parameters["limit"]?.toIntOrNull() ?: paginator.limit

            return NextValues(offset, limit)
        }
    }
}