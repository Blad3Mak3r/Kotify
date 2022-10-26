package tv.blademaker.kotify.internal

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

data class ContextAccessToken(val value: String) : AbstractCoroutineContextElement(ContextAccessToken){

    companion object Key : CoroutineContext.Key<ContextAccessToken>

}