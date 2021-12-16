package tv.blademaker.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtistTopTracks(
    val tracks: List<Track>
)