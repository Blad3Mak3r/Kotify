package tv.blademaker.kotify.services

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.AuthorizationResponse
import tv.blademaker.kotify.models.RefreshTokenResponse
import tv.blademaker.kotify.parse

class AuthorizationService(override val kotify: Kotify) : Service {

    fun buildAuthorizationCodeFlow(redirectUri: String, scopes: List<Kotify.Scope>, state: String? = null): Url {
        val builder = URLBuilder("https://accounts.spotify.com/authorize")

        builder.parameters.apply {
            append("client_id", kotify.credentials.clientId)
            append("response_type", "code")
            append("redirect_uri", redirectUri)
            state?.let { append("state", it) }
            append("scope", scopes.parse())
        }

        return builder.build()
    }

    /**
     * Retrieve an Access Token for the given code and redirect uri.
     *
     * @param code The code from Spotify Login.
     * @param redirectUri The redirect url used to retrieve the code.
     *
     * @return A [AuthorizationResponse] with the access token, scopes and expiration.
     *
     * @throws tv.blademaker.kotify.exceptions.KotifyRequestException
     */
    private suspend fun retrieveAccessToken(code: String, redirectUri: String): AuthorizationResponse {
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

    /**
     * Retrieve a fresh Access Token for the give Refresh Token.
     *
     * @param refreshToken The refresh token
     *
     * @return A [RefreshTokenResponse] with the new access token, scopes and expiration.
     *
     * @throws tv.blademaker.kotify.exceptions.KotifyRequestException
     */
    private suspend fun refreshAccessToken(refreshToken: String): RefreshTokenResponse {
        return kotify.httpClient.submitForm {
            url("https://accounts.spotify.com/api/token")
            headers {
                append("Authorization", kotify.credentials.basicAuthHeader)
            }
            parameter("grant_type", "refresh_token")
            parameter("refresh_token", refreshToken)
        }.body()
    }
}