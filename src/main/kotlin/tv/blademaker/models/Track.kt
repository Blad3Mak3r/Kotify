package tv.blademaker.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val id: String,
    val name: String,
    val album: PartialAlbum? = null,
    val artists: List<Artist>,
    @SerialName("available_markets") val availableMarkets: List<String>,
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

    @Serializable
    data class ExternalIds(
        val isrc: String? = null,
        val ean: String? = null,
        val upc: String? = null
    )
}

@Serializable
data class PartialTrack(
    val id: String,
    val name: String,
    val uri: String
)

typealias TrackList = List<Track>