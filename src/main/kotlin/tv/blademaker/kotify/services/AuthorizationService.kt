package tv.blademaker.kotify.services

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.AuthorizationResponse
import tv.blademaker.kotify.services.request

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
        return kotify.httpClient.submitForm {
            url("https://accounts.spotify.com/api/token")
            headers {
                append("Authorization", kotify.credentials.basicAuthHeader)
            }
            parameter("code", code)
            parameter("client_id", kotify.credentials.clientId)
            parameter("client_secret", kotify.credentials.clientSecret)
            parameter("grant_type", "client_credentials")
            parameter("redirect_uri", redirectUri)
        }.body()
    }

}