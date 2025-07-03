package io.github.blad3mak3r.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val id: String,
    val name: String,
    val album: SimplifiedAlbum? = null,
    val artists: List<Artist>,
    @SerialName("available_markets") val availableMarkets: List<String>? = null,
    @SerialName("disc_number") val discNumber: Int = 1,
    @SerialName("duration_ms") val durationMillis: Long,
    val explicit: Boolean,
    @SerialName("external_ids") val externalIds: ExternalIds? = null,
    @SerialName("external_urls") val externalUrls: Map<String, String>,
    val href: String,
    @SerialName("is_playable") val isPlayable: Boolean? = null,
    val restrictions: Restrictions? = null,
    val popularity: Int? = null,
    @SerialName("preview_url") val previewUrl: String? = null,
    @SerialName("track_number") val trackNumber: Int? = null,
    val uri: String,
    @SerialName("is_local") val isLocal: Boolean
) {

    val openUrl: String
        get() = "https://open.spotify.com/track/$id"

    @Serializable
    data class ExternalIds(
        val isrc: String? = null,
        val ean: String? = null,
        val upc: String? = null
    )
}