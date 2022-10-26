package tv.blademaker.kotify.utils

import tv.blademaker.kotify.models.Track

fun Collection<Track>.withISRC() = this.filter { it.externalIds?.isrc != null }