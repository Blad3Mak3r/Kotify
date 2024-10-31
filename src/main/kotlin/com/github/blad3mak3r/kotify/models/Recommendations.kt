package com.github.blad3mak3r.kotify.models

@kotlinx.serialization.Serializable
data class Recommendations(
    val seeds: List<Seed>,
    val tracks: List<Track>
)