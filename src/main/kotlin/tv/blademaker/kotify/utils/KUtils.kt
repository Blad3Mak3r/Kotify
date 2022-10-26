package tv.blademaker.kotify.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tv.blademaker.kotify.internal.ContextAccessToken
import tv.blademaker.kotify.services.Service

suspend fun <T : Any, S : Service> withAccessToken(accessToken: String, service: S, block: suspend S.() -> T): T {
    return withContext(
        Dispatchers.IO + ContextAccessToken(accessToken)
    ) {
        block(service)
    }
}