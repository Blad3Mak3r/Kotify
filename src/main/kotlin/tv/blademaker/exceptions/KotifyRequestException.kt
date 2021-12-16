package tv.blademaker.exceptions

import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.statement.*
import io.ktor.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class KotifyRequestException(
    response: HttpResponse,
    val error: Error
) : Exception() {

    val status = response.status.value
    val description = response.status.description

    val headers = response.headers.toMap()

    companion object {
        suspend fun complete(deferred: CompletableDeferred<*>, ex: ClientRequestException) {
            deferred.completeExceptionally(from(ex))
        }

        private suspend fun from(ex: ClientRequestException): KotifyRequestException {
            val error = ex.response.receive<ErrorHolder>().error

            return KotifyRequestException(ex.response, error)
        }
    }
}