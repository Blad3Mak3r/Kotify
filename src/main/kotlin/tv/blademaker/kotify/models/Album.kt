package tv.blademaker.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tv.blademaker.kotify.Kotify

@Serializable
enum class AlbumType {
    @SerialName("album") ALBUM,
    @SerialName("single") SINGLE,
    @SerialName("compilation") COMPILATION
}

@Serializable
data class Album(
    val id: String,
    val name: String,
    @SerialName("album_type") val albumType: AlbumType? = null,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>? = null,
    @SerialName("external_urls") val externalUrls: Map<String, String>,
    val href: String,
    val images: List<Image>,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String,
    val restrictions: Restrictions? = null,
    val uri: String,
    val artist: List<Artist>? = null,
    val tracks: AlbumPagination
)

@Serializable
data class PartialAlbum(
    val id: String,
    val name: String,
    val uri: String,
    @SerialName("album_type") val albumType: AlbumType? = null,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>? = null,
    @SerialName("external_urls") val externalUrls: Map<String, String>,
    val href: String,
    val images: List<Image>,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String,
    val type: String
)