package com.github.blad3mak3r.kotify.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val status: Int,
    val message: String
)

@Serializable
data class ErrorHolder(
    val error: Error
)