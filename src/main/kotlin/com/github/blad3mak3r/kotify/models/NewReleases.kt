package com.github.blad3mak3r.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class NewReleases(
    val albums: AlbumsPagination
)
