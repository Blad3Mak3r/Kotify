package tv.blademaker.kotify.services

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.AuthorizationResponse

class AuthorizationService(override val kotify: Kotify) : Service {

    @Serializable
    private data class AccessTokenRequestBody(
        val code: String,
        @SerialName("redirect_uri") val redirectURL: String,
        val scope: String
    )

    fun getAuthorizationUrl(redirectUri: String, scope: String, state: String? = null): Url {
        val builder = URLBuilder("https://accounts.spotify.com/authorize")

        builder.parameters.apply {
            append("client_id", kotify.credentials.clientId)
            append("response_type", "code")
            append("redirect_uri", redirectUri)
            state?.let { append("state", it) }
            append("scope", scope)

        }

        return builder.build()
    }

    private suspend fun retrieveAuthorizeCode(code: String, redirectUri: String): AuthorizationResponse {

        val urlBuilder = URLBuilder("https://accounts.spotify.com/api/token")

        urlBuilder.parameters.apply {
            append("client_id", kotify.credentials.clientId)
            append("grant_type", "authorization_code")
            append("code", code)
            append("redirect_uri", redirectUri)
        }

        val url = urlBuilder.build()
        return request(AuthorizationResponse.serializer(), {
            baseUrl = "https://accounts.spotify.com"
            path = url.fullPath
            method = HttpMethod.Post
            headers.set("authorization", kotify.credentials.basicAuthHeader)
            headers.append("content-type", "x-www-form-urlencoded")
        }, {})
    }

}