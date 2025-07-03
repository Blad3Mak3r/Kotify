package io.github.blad3mak3r.kotify.internal

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ClientCredentials(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    val scope: String? = null,
    @SerialName("expires_in") val expiresIn: Long
) {

    val expiresAt = System.currentTimeMillis() + (expiresIn * 1000)

}