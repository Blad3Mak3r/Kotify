package tv.blademaker.kotify.request

import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.request
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
    requestConfiguration: RequestConfiguration.() -> Unit
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
        val config = RequestConfiguration().apply(requestConfiguration)

        accessToken = config.accessToken
        baseUrl = builder.baseUrl
        path = builder.path
        method = builder.method
        url = baseUrl + path
    }

    suspend fun queue(kotify: Kotify): T {
        kotify.enqueue(this)

        return deferred.await()
    }

    fun queueAsync(kotify: Kotify): Deferred<T> {
        kotify.enqueue(this)

        return deferred
    }

    suspend fun execute(kotify: Kotify): Boolean = coroutineScope {
        try {
            val auth = kotify.credentials.getAccessToken()

            val response = kotify.httpClient.request<HttpResponse> {
                url(this@Request.url)
                method = this@Request.method
                headers {
                    append("authorization", "Bearer $auth")
                }
            }

            val content = json.decodeFromString(serializer, response.receive())
            deferred.complete(content)
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
                    kotify.enqueueFirst(this@Request)
                    return@coroutineScope false
                }
                else -> KotifyRequestException.complete(deferred, ex)
            }

        } catch (e: Exception) {
            Kotify.log.error("Handled exception executing request ${this@Request}: ${e.message}", e)
            deferred.completeExceptionally(e)
        }

        return@coroutineScope true
    }

    fun cancel() {
        deferred.cancel()
    }

    override fun toString(): String {
        return "[${method.value}] -> (${url})"
    }

    internal class Builder {
        var baseUrl: String = Kotify.baseUrl
        var path: String = ""
        var method: HttpMethod = HttpMethod.Get
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}