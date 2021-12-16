package tv.blademaker.models

import kotlinx.serialization.Serializable

@Serializable
data class Restrictions(
    val reason: Reason
) {
    enum class Reason(
        val value: String
    ) {
        MARKET("market"),
        PRODUCT("product"),
        EXPLICIT("explicit"),
        UNKNOWN("unknown")
    }
}
