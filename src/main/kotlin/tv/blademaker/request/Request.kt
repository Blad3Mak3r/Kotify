package tv.blademaker.request

import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.request.request
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tv.blademaker.Kotify
import java.util.concurrent.atomic.AtomicReference

class Request<T : Any>(requestBuilder: Builder<T>.() -> Unit) {

    val path: String
    val method: HttpMethod
    private val serializer: KSerializer<T>

    private val deferred = CompletableDeferred<T>()

    private val value = AtomicReference<T>(null)

    init {
        val builder = Builder<T>().apply(requestBuilder)

        path = builder.path
        method = builder.method
        serializer = builder.serializer!!
    }

    suspend fun queue(kotify: Kotify): T {
        kotify.enqueue(this)

        return deferred.await()
    }

    suspend fun execute(kotify: Kotify) = coroutineScope {
        val url = kotify.baseUrl + path

        println("Executing request $url")

        try {
            val auth = kotify.credentials.getAccessToken()

            val response = kotify.httpClient.request<HttpResponse> {
                url(url)
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
                retryAfter?.toLongOrNull()?.let { kotify.retryAfterRef.set(it * 1000) }
            }

            deferred.completeExceptionally(ex)
        }
    }

    class Builder<T : Any>(
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