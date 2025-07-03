package io.github.blad3mak3r.kotify.models

@kotlinx.serialization.Serializable
data class SeveralTracksPage(
    val tracks: List<Track>
)