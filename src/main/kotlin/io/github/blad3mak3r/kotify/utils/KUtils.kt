package io.github.blad3mak3r.kotify.utils

import io.github.blad3mak3r.kotify.internal.ContextAccessToken
import io.github.blad3mak3r.kotify.services.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T : Any, S : Service> withAccessToken(accessToken: String, service: S, block: suspend S.() -> T): T {
    return withContext(
        Dispatchers.IO + ContextAccessToken(accessToken)
    ) {
        block(service)
    }
}