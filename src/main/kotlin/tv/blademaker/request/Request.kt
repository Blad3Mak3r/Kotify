package tv.blademaker.request

import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.request
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tv.blademaker.Kotify
import java.util.concurrent.atomic.AtomicReference

class Request<T : Any>(requestBuilder: Builder<T>.() -> Unit) {

    private val baseUrl: String
    private val path: String
    val method: HttpMethod
    val url: String
    private val serializer: KSerializer<T>

    private val deferred = CompletableDeferred<T>()

    private val value = AtomicReference<T>(null)

    init {
        val builder = Builder<T>().apply(requestBuilder)

        baseUrl = builder.baseUrl
        path = builder.path
        method = builder.method
        url = baseUrl + path
        serializer = builder.serializer!!
    }

    suspend fun queue(kotify: Kotify): T {
        kotify.enqueue(this)

        return deferred.await()
    }

    suspend fun queueAsync(kotify: Kotify): Deferred<T> {
        kotify.enqueue(this)

        return deferred
    }

    suspend fun execute(kotify: Kotify): Boolean = coroutineScope {
        println("Executing request $url")

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

            if (status == 429) {
                Kotify.log.warn("Encountered 429 on request ${this@Request} with retry-after header of $retryAfter seconds.")
                val retryAfterValue = retryAfter?.toIntOrNull()
                retryAfterValue?.let {
                    kotify.retryAfter = (it * 1000L + 1200L)
                }
                kotify.enqueueFirst(this@Request)
                return@coroutineScope false
            }

            deferred.completeExceptionally(ex)
        }

        return@coroutineScope true
    }

    fun cancel() {
        deferred.cancel()
    }

    override fun toString(): String {
        return "[${method.value}] -> (${url})"
    }

    class Builder<T : Any>(
        var baseUrl: String = Kotify.baseUrl,
        var path: String = "",
        var method: HttpMethod = HttpMethod.Get,
        var serializer: KSerializer<T>? = null
    )

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}