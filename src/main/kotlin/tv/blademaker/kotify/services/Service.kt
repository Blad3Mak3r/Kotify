package tv.blademaker.kotify.services

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.request.Request
import tv.blademaker.kotify.request.RequestConfiguration

interface Service {
    val kotify: Kotify

    companion object {
        inline fun <reified T : Service> of(kotify: Kotify): T {
            return T::class.java.getDeclaredConstructor(Kotify::class.java).newInstance(kotify)
        }
    }
}

internal suspend fun <T : Any> Service.request(
    serializer: KSerializer<T>,
    builder: Request.Builder.() -> Unit,
    config: RequestConfiguration
): T = withContext(Dispatchers.IO) {
    Request(serializer, builder, config).queue(kotify)
}

internal suspend fun <T : Any> Service.request(
    serializer: KSerializer<T>,
    builder: Request.Builder.() -> Unit,
    config: RequestConfiguration.() -> Unit
): T = withContext(Dispatchers.IO) {
    Request(serializer, builder, RequestConfiguration().apply(config)).queue(kotify)
}

internal suspend fun <T : Any> Service.requestAsync(
    serializer: KSerializer<T>,
    builder: Request.Builder.() -> Unit,
    config: RequestConfiguration
): Deferred<T> = withContext(Dispatchers.IO) {
    Request(serializer, builder, config).queueAsync(kotify)
}

internal suspend fun <T : Any> Service.requestAsync(
    serializer: KSerializer<T>,
    builder: Request.Builder.() -> Unit,
    config: RequestConfiguration.() -> Unit
): Deferred<T> = withContext(Dispatchers.IO) {
    Request(serializer, builder, RequestConfiguration().apply(config)).queueAsync(kotify)
}