package io.github.blad3mak3r.kotify.models

import io.github.blad3mak3r.kotify.request.RequestConfiguration

class RecommendationsRequestConfiguration : RequestConfiguration() {

    var seedArtists: List<String>? = null
    var seedGenres: List<String>? = null
    var seedTracks: List<String>? = null
    var limit: Int = 100
    var market: String? = null
    var maxAcousticness: Double? = null
    var maxDanceability: Double? = null
    var maxDurationMs: Long? = null
    var maxEnergy: Double? = null
    var maxInstrumentalness: Double? = null
    var maxKey: Int? = null
    val maxLiveness: Double? = null
    var maxLoudness: Double? = null
    var maxMode: Int? = null
    var maxPopularity: Int? = null
    var maxSpeechiness: Double? = null
    var maxTempo: Double? = null
    var maxTimeSignature: Long? = null
    var maxValence: Double? = null
    var minAcousticness: Double? = null
    var minDanceability: Double? = null
    var minDurationMs: Long? = null
    var minEnergy: Double? = null
    var minInstrumentalness: Double? = null
    var minKey: Int? = null
    val minLiveness: Double? = null
    var minLoudness: Double? = null
    var minMode: Int? = null
    var minPopularity: Int? = null
    var minSpeechiness: Double? = null
    var minTempo: Double? = null
    var minTimeSignature: Long? = null
    var minValence: Double? = null
    var targetAcousticness: Double? = null
    var targetDanceability: Double? = null
    var targetDurationMs: Long? = null
    var targetEnergy: Double? = null
    var targetInstrumentalness: Double? = null
    var targetKey: Int? = null
    val targetLiveness: Double? = null
    var targetLoudness: Double? = null
    var targetMode: Int? = null
    var targetPopularity: Int? = null
    var targetSpeechiness: Double? = null
    var targetTempo: Double? = null
    var targetTimeSignature: Long? = null
    var targetValence: Double? = null

}