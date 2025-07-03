package io.github.blad3mak3r.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    val scope: String? = null,
    @SerialName("expires_in") val expiresIn: Long
) {

    val expiresAt = System.currentTimeMillis() + (expiresIn * 1000)

}