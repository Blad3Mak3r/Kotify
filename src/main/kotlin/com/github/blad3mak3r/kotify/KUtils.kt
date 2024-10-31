package com.github.blad3mak3r.kotify

internal fun Iterable<Kotify.Scope>.parse(): String {
    return joinToString("+") { it.value }
}