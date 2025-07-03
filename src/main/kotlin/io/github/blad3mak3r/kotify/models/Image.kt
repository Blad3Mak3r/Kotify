package io.github.blad3mak3r.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val url: String,
    val height: Int? = null,
    val width: Int? = null
)
