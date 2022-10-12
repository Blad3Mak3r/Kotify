package tv.blademaker.kotify.request

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.exceptions.KotifyException
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
    private val method: HttpMethod
    private val url: URLBuilder

    private val value = AtomicReference<T>(null)

    init {
        val builder = Builder().apply(requestBuilder)

        accessToken = config.accessToken
        baseUrl = builder.baseUrl
        path = builder.path
        method = builder.method
        url = URLBuilder(baseUrl + path)

        if (builder.query.isNotEmpty()) {
            for (q in builder.query) {
                url.parameters.append(q.key, q.value)
            }
        }
    }

    suspend fun execute(kotify: Kotify): T = coroutineScope {
        kotify.getDelay?.let { delay(it) }

        try {
            val auth = accessToken ?: kotify.credentials.getAccessToken()

            val response = kotify.httpClient.request {
                url(this@Request.url.build())
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

        } catch (e: Throwable) {
            throw KotifyException(e.message, e)
        }
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
        val query = HashMap<String, String>()
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}