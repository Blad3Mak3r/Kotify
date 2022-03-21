package tv.blademaker.kotify.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.request.Request
import tv.blademaker.kotify.request.RequestConfiguration

interface Service {
    val kotify: Kotify
}

internal suspend fun <T : Any> Service.request(
    serializer: KSerializer<T>,
    builder: Request.Builder.() -> Unit,
    config: RequestConfiguration
): T = withContext(Dispatchers.IO) {
    Request(serializer, builder, config).execute(kotify)
}

internal suspend fun <T : Any> Service.request(
    serializer: KSerializer<T>,
    builder: Request.Builder.() -> Unit,
    config: RequestConfiguration.() -> Unit
): T = withContext(Dispatchers.IO) {
    Request(serializer, builder, RequestConfiguration().apply(config)).execute(kotify)
}