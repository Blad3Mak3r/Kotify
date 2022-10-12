package tv.blademaker.kotify.exceptions

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.util.*
import kotlinx.coroutines.CompletableDeferred
import java.io.IOException

open class KotifyException(override val message: String? = null, override val cause: Throwable? = null) : IOException()

@Suppress("unused", "MemberVisibilityCanBePrivate")
class KotifyRequestException private constructor(
    override val cause: ClientRequestException, sup: KotifySuppressedInfo
) : KotifyException(message = "${cause.response.status.value} ${cause.response.status.description}") {

    val status = cause.response.status.value
    val description = cause.response.status.description

    val headers = cause.response.headers.toMap()

    init {
        addSuppressed(sup)
    }

    companion object {
        suspend fun from(ex: ClientRequestException): KotifyRequestException {
            val content = ex.response.bodyAsText()
            return KotifyRequestException(ex, KotifySuppressedInfo(ex.response, content))
        }
    }
}

class KotifySuppressedInfo(response: HttpResponse, content: String) : Throwable(buildString {
    appendLine("response.status.code: ${response.status.value}")
    appendLine("response.status.message: ${response.status.description}")
    appendLine("response.http.version: ${response.version}")
    appendLine("response.content: $content")
})