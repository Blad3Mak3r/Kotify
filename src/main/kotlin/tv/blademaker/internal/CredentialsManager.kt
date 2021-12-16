package tv.blademaker.internal

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.request
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tv.blademaker.Kotify
import tv.blademaker.models.AuthorizationResponse
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class CredentialsManager internal constructor(
    private val kotify: Kotify,
    private val clientId: String,
    private val clientSecret: String
){
    private val accessTokenRef = AtomicReference<String?>(null)

    private val expireStampRef = AtomicLong(-1L)

    private val expireStamp: Long
        get() = expireStampRef.get()

    private val isExpired: Boolean
        get() = accessTokenRef.get() == null || expireStamp == -1L || expireStamp < System.currentTimeMillis()

    internal suspend fun getAccessToken(): String {
        val ref = accessTokenRef.get()
        if (ref == null || isExpired) {
            updateAccessToken()
        }

        return accessTokenRef.get()!!
    }

    private suspend fun updateAccessToken() {
        val token = retrieveAccessToken()
        accessTokenRef.set(token.accessToken)
        expireStampRef.set(token.expiresAt)
    }

    private suspend fun retrieveAccessToken(): AuthorizationResponse {
        val response = kotify.httpClient.request<HttpResponse> {
            method = HttpMethod.Post
            url("https://accounts.spotify.com/api/token?grant_type=client_credentials")
            headers {
                append("Authorization", "Basic ${kotify.encodedCredentials}")
                append("content-type", "application/x-www-form-urlencoded")
            }
        }

        println(response.status.value)

        return response.receive()
    }

}