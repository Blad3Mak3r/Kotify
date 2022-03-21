package tv.blademaker.kotify

import java.net.URLEncoder
import java.nio.charset.Charset

internal fun Iterable<Kotify.Scope>.parse(): String {
    return URLEncoder.encode(joinToString(" ") { it.value }, Charset.defaultCharset())
}