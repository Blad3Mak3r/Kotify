package tv.blademaker.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class TracksSearchResult(
    val tracks: TracksPagination
)