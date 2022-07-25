package tv.blademaker.kotify.internal

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import tv.blademaker.kotify.Kotify
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

class CredentialsManager internal constructor(
    private val kotify: Kotify,
    val clientId: String,
    private val clientSecret: String
) : Interceptor{
    private val basicAuthHeader = "Basic ${Base64.getEncoder().encodeToString("$clientId:$clientSecret".toByteArray())}"

    private val accessTokenRef = AtomicReference<String>(null)

    private val expireStampRef = AtomicLong(0L)

    private val expireStamp: Long
        get() = expireStampRef.get()

    private val isExpired: Boolean
        get() = accessTokenRef.get() == null || expireStamp < System.currentTimeMillis()

    private fun getAccessToken(chain: Interceptor.Chain): String {
        val ref = accessTokenRef.get()
        if (ref == null || isExpired) {
            if (isExpired) Kotify.log.debug("Token expired.")
            updateAccessToken(chain)
        }
        return accessTokenRef.get()
    }

    private fun updateAccessToken(chain: Interceptor.Chain) {
        val token = retrieveAccessToken(chain)
        accessTokenRef.set(token.accessToken)
        expireStampRef.set(token.expiresAt)
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun retrieveAccessToken(chain: Interceptor.Chain): ClientCredentials {
        Kotify.log.info("Retrieving new access token for client $clientId.")

        val body = FormBody.Builder().apply {
            add("grant_type", "client_credentials")
        }.build()
        val request = Request.Builder()
            .addHeader("Authorization", basicAuthHeader)
            .url("https://accounts.spotify.com/api/token")
            .post(body)
            .build()

        Kotify.log.debug("Requesting accessToken for $clientId.")
        val res = chain.proceed(request)

        Kotify.log.debug("Checking accessToken request status for $clientId.")
        check(res.isSuccessful) {
            "Non success status code requesting accessToken: ${res.code}"
        }

        Kotify.log.debug("Parsing accessToken request body for $clientId.")
        val credentials: ClientCredentials = res.body?.byteStream()?.let {
            Kotify.JSON.decodeFromStream(ClientCredentials.serializer(), it)
        }
            ?: error("Empty body on request")

        Kotify.log.info("Got new access token for client $clientId")

        return credentials
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getAccessToken(chain)

        val req = chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()

        return chain.proceed(req)
    }

}