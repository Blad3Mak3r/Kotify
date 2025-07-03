package io.github.blad3mak3r.kotify.utils

import io.github.blad3mak3r.kotify.models.Track

fun Collection<Track>.withISRC() = this.filter { it.externalIds?.isrc != null }