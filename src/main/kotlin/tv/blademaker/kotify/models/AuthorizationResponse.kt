package tv.blademaker.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String,
    val scope: String? = null,
    @SerialName("expires_in") val expiresIn: Long
) {

    val expiresAt = System.currentTimeMillis() + (expiresIn * 1000)

}