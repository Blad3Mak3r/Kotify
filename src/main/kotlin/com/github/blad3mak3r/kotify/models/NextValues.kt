package com.github.blad3mak3r.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class NextValues(
    val offset: Int,
    val limit: Int
)
