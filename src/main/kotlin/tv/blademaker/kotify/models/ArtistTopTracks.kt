package tv.blademaker.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class ArtistTopTracks(
    val tracks: List<Track>
)