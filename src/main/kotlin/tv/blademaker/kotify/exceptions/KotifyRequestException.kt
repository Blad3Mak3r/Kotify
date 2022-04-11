package tv.blademaker.kotify.exceptions

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.util.*
import kotlinx.coroutines.CompletableDeferred

open class KotifyException(override val message: String? = null, override val cause: Throwable? = null) : RuntimeException()

@Suppress("unused", "MemberVisibilityCanBePrivate")
class KotifyRequestException(
    response: HttpResponse,
    val error: String
) : KotifyException(message = "${response.status.value} $error") {

    val status = response.status.value
    val description = response.status.description

    val headers = response.headers.toMap()

    companion object {
        suspend fun complete(deferred: CompletableDeferred<*>, ex: ClientRequestException) {
            deferred.completeExceptionally(from(ex))
        }

        suspend fun from(ex: ClientRequestException): KotifyRequestException {
            val error = ex.response.bodyAsText()

            return KotifyRequestException(ex.response, error)
        }
    }
}