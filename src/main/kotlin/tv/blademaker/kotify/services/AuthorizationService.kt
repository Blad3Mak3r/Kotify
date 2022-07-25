package tv.blademaker.kotify.services

import tv.blademaker.kotify.models.AuthorizationResponse

interface AuthorizationService {

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
    /*suspend fun retrieveAccessToken(code: String, redirectUri: String): AuthorizationResponse {
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
    suspend fun refreshAccessToken(refreshToken: String): RefreshTokenResponse {
        return kotify.httpClient.submitForm {
            url("https://accounts.spotify.com/api/token")
            headers {
                append("Authorization", kotify.credentials.basicAuthHeader)
            }
            parameter("grant_type", "refresh_token")
            parameter("refresh_token", refreshToken)
        }.body()
    }*/
}
