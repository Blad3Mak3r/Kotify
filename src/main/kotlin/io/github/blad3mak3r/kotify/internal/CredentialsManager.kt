package io.github.blad3mak3r.kotify.internal

import io.github.blad3mak3r.kotify.Kotify
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class CredentialsManager internal constructor(
    private val kotify: Kotify,
    internal val clientId: String,
    internal val clientSecret: String
){
    internal val basicAuthHeader = "Basic ${Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())}"

    private val accessTokenRef = AtomicReference<String>(null)

    private val expireStampRef = AtomicLong(0L)

    private val expireStamp: Long
        get() = expireStampRef.get()

    private val isExpired: Boolean
        get() = accessTokenRef.get() == null || expireStamp < System.currentTimeMillis()

    internal suspend fun getAccessToken(): String {
        val ref = accessTokenRef.get()
        if (ref == null || isExpired) {
            if (isExpired) Kotify.log.debug("Token expired.")
            updateAccessToken()
        }
        return accessTokenRef.get()
    }

    private suspend fun updateAccessToken() {
        val token = retrieveAccessToken()
        accessTokenRef.set(token.accessToken)
        expireStampRef.set(token.expiresAt)
    }

    private suspend fun retrieveAccessToken(): ClientCredentials {
        Kotify.log.info("Retrieving new access token for client $clientId.")
        val response = kotify.httpClient.submitForm {
            url("https://accounts.spotify.com/api/token")
            headers {
                append("Authorization", basicAuthHeader)
            }
            parameter("grant_type", "client_credentials")
        }

        val res = response.body<ClientCredentials>()

        Kotify.log.info("Got new access token for client $clientId")

        return res
    }

}