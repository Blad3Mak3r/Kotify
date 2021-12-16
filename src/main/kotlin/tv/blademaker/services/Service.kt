package tv.blademaker.services

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tv.blademaker.Kotify
import tv.blademaker.request.Request
import kotlin.reflect.KClass

interface Service {
    val kotify: Kotify

}

suspend inline fun <reified T : Any> Service.request(crossinline requestBuilder: Request.Builder<T>.() -> Unit): T = withContext(Dispatchers.IO) {
    Request<T> { apply(requestBuilder) }.queue(kotify)
}

suspend inline fun <reified T : Any> Service.requestAsync(crossinline requestBuilder: Request.Builder<T>.() -> Unit): Deferred<T> = withContext(Dispatchers.IO) {
    Request<T> { apply(requestBuilder) }.queueAsync(kotify)
}