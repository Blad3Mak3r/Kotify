package tv.blademaker.services

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tv.blademaker.Kotify
import tv.blademaker.request.Request

interface Service {
    val kotify: Kotify

    companion object {
        inline fun <reified T : Service> of(kotify: Kotify): T {
            return T::class.java.getDeclaredConstructor(Kotify::class.java).newInstance(kotify)
        }
    }
}

suspend inline fun <reified T : Any> Service.request(crossinline requestBuilder: Request.Builder<T>.() -> Unit): T = withContext(Dispatchers.IO) {
    Request<T> { apply(requestBuilder) }.queue(kotify)
}

suspend inline fun <reified T : Any> Service.requestAsync(crossinline requestBuilder: Request.Builder<T>.() -> Unit): Deferred<T> = withContext(Dispatchers.IO) {
    Request<T> { apply(requestBuilder) }.queueAsync(kotify)
}