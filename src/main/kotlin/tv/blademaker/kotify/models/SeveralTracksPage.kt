package tv.blademaker.kotify.models

@kotlinx.serialization.Serializable
data class SeveralTracksPage(
    val tracks: List<Track>
)