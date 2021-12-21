package tv.blademaker.kotify.internal

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.AuthorizationResponse
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class CredentialsManager internal constructor(
    private val kotify: Kotify,
    private val clientId: String,
    private val clientSecret: String
){
    private val basicAuthHeader = "Basic ${Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())}"

    private val accessTokenRef = AtomicReference<String>(null)

    private val expireStampRef = AtomicLong(0L)

    private val expireStamp: Long
        get() = expireStampRef.get()

    private val isExpired: Boolean
        get() = accessTokenRef.get() == null || expireStamp < System.currentTimeMillis()

    internal suspend fun getAccessToken(): String {
        val ref = accessTokenRef.get()
        if (ref == null || isExpired) {
            if (isExpired) tv.blademaker.kotify.Kotify.log.debug("Token expired.")
            updateAccessToken()
        }
        return accessTokenRef.get()
    }

    private suspend fun updateAccessToken() {
        val token = retrieveAccessToken()
        accessTokenRef.set(token.accessToken)
        expireStampRef.set(token.expiresAt)
    }

    private suspend fun retrieveAccessToken(): AuthorizationResponse {
        tv.blademaker.kotify.Kotify.log.info("Retrieving new access token for client $clientId.")
        val response = kotify.httpClient.request<HttpResponse> {
            method = HttpMethod.Post
            url("https://accounts.spotify.com/api/token?grant_type=client_credentials")
            headers {
                append("Authorization", basicAuthHeader)
                append("content-type", "application/x-www-form-urlencoded")
            }
        }

        return response.receive()
    }

}