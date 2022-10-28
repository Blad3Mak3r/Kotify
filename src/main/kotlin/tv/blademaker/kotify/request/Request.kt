package tv.blademaker.kotify.request

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.exceptions.KotifyException
import tv.blademaker.kotify.exceptions.KotifyRequestException
import tv.blademaker.kotify.internal.ContextAccessToken
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext

class Request<T : Any>(
    private val kotify: Kotify,
    private val serializer: KSerializer<T>,
    private var method: HttpMethod,
    private val path: String
    ) {

    private val url: URLBuilder = URLBuilder(Kotify.baseUrl).apply {
        path(path)
    }

    fun addQuery(name: String, value: String): Request<T> {
        url.parameters.append(name, value)
        return this
    }

    fun limit(value: Int): Request<T> {
        url.parameters["limit"] = value.toString()
        return this
    }

    fun offset(value: Int): Request<T> {
        url.parameters["offset"] = value.toString()
        return this
    }

    fun addQuery(name: String, value: Int): Request<T> {
        url.parameters.append(name, value.toString())
        return this
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun execute(): T = coroutineScope {
        kotify.getDelay?.let { delay(it) }

        try {
            val auth = this.coroutineContext[ContextAccessToken]?.value
                ?: kotify.credentials.getAccessToken()

            val response = kotify.httpClient.request {
                url(this@Request.url.build())
                method = this@Request.method
                headers {
                    set("Authorization", "Bearer $auth")
                    contentType(ContentType("application", "json"))
                }
            }

            if (serializer == Unit.serializer()) {
                return@coroutineScope Unit as T
            }
            json.decodeFromString(serializer, response.bodyAsText())
        } catch (ex: ClientRequestException) {
            val status = ex.response.status.value
            val retryAfter = ex.response.headers["retry-after"]

            when (status) {
                429 -> {
                    Kotify.log.warn("Encountered 429 on request ${this@Request} with retry-after header of $retryAfter seconds.")
                    val retryAfterValue = retryAfter?.toIntOrNull()
                    retryAfterValue?.let {
                        kotify.retryAfter = (it * 1000L + 1200L)
                    }
                    delay(kotify.retryAfter)
                    return@coroutineScope execute()
                }
                else -> {
                    Kotify.log.error("Handled exception executing request ${this@Request}: ${ex.message}", ex)
                    throw KotifyRequestException.from(ex)
                }
            }

        } catch (e: Throwable) {
            throw e
        }
    }

    override fun toString(): String {
        return "[${method.value}] -> (${url})"
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}