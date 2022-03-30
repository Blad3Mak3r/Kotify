package tv.blademaker.kotify.models

@kotlinx.serialization.Serializable
data class Recommendations(
    val seeds: List<Seed>,
    val tracks: List<Track>
)