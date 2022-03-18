package tv.blademaker.kotify.exceptions

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.ktor.util.*
import kotlinx.coroutines.CompletableDeferred

@Suppress("unused", "MemberVisibilityCanBePrivate")
class KotifyRequestException(
    response: HttpResponse,
    val error: String
) : Exception() {

    val status = response.status.value
    val description = response.status.description

    val headers = response.headers.toMap()

    override val message: String = "$status $error"

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