package com.github.blad3mak3r.kotify.utils

import com.github.blad3mak3r.kotify.internal.ContextAccessToken
import com.github.blad3mak3r.kotify.services.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T : Any, S : Service> withAccessToken(accessToken: String, service: S, block: suspend S.() -> T): T {
    return withContext(
        Dispatchers.IO + ContextAccessToken(accessToken)
    ) {
        block(service)
    }
}