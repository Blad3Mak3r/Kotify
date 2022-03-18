package tv.blademaker.kotify.request

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.exceptions.KotifyRequestException
import java.util.concurrent.atomic.AtomicReference

internal class Request<T : Any>(
    private val serializer: KSerializer<T>,
    requestBuilder: Builder.() -> Unit,
    config: RequestConfiguration
) {

    private val accessToken: String?
    private val baseUrl: String
    private val path: String
    val method: HttpMethod
    val url: String

    private val deferred = CompletableDeferred<T>()

    private val value = AtomicReference<T>(null)

    init {
        val builder = Builder().apply(requestBuilder)

        accessToken = config.accessToken
        baseUrl = builder.baseUrl
        path = builder.path
        method = builder.method
        url = baseUrl + path
    }

    suspend fun executeAsync(kotify: Kotify): Deferred<T> = coroutineScope { async { execute(kotify) } }

    suspend fun execute(kotify: Kotify): T = coroutineScope {
        kotify.getDelay?.let { delay(it) }

        try {
            val auth = accessToken ?: kotify.credentials.getAccessToken()

            val response = kotify.httpClient.request {
                url(this@Request.url)
                method = this@Request.method
                headers {
                    set("Authorization", "Bearer $auth")
                    contentType(ContentType("application", "json"))
                }
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
                    return@coroutineScope execute(kotify)
                }
                else -> {
                    Kotify.log.error("Handled exception executing request ${this@Request}: ${ex.message}", ex)
                    throw KotifyRequestException.from(ex)
                }
            }

        } catch (e: Exception) {
            throw e
        }
    }

    fun cancel() {
        deferred.cancel()
    }

    override fun toString(): String {
        return "[${method.value}] -> (${url})"
    }

    @Suppress("unused")
    internal class Builder {
        var baseUrl: String = Kotify.baseUrl
        var path: String = ""
        var method: HttpMethod = HttpMethod.Get
        var body: Any? = null
        val headers = HeadersBuilder()
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}