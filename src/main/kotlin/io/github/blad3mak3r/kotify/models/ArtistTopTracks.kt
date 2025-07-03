package io.github.blad3mak3r.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class ArtistTopTracks(
    val tracks: List<Track>
)