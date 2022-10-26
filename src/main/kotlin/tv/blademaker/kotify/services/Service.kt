package tv.blademaker.kotify.services

import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.KSerializer
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.internal.ContextAccessToken
import tv.blademaker.kotify.request.Request
import tv.blademaker.kotify.request.RequestConfiguration
import kotlin.coroutines.CoroutineContext

interface Service {

    val kotify: Kotify
}

internal suspend inline fun <reified T: Any> Service.get(path: String, serializer: KSerializer<T>): Request<T> {
    return this.kotify.newRequest(HttpMethod.Get, path, serializer)
}