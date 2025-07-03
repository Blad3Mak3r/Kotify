package io.github.blad3mak3r.kotify.services

import io.github.blad3mak3r.kotify.Kotify
import io.github.blad3mak3r.kotify.request.Request
import io.ktor.http.*
import kotlinx.serialization.KSerializer

interface Service {

    val kotify: Kotify
}

internal suspend inline fun <reified T: Any> Service.get(path: String, serializer: KSerializer<T>): Request<T> {
    return this.kotify.newRequest(HttpMethod.Get, path, serializer)
}