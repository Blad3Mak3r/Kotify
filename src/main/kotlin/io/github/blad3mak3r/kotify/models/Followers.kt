package io.github.blad3mak3r.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class Followers(
    val href: String? = null,
    val total: Int
)
