package io.github.blad3mak3r.kotify.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    val id: String,
    val name: String,
    @SerialName("album_type") val albumType: String,
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
data class SimplifiedAlbum(
    val id: String,
    val name: String,
    val uri: String,
    @SerialName("album_type") val albumType: String,
    @SerialName("total_tracks") val totalTracks: Int,
    @SerialName("available_markets") val availableMarkets: List<String>? = null,
    @SerialName("external_urls") val externalUrls: Map<String, String>,
    val href: String,
    val images: List<Image>,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("release_date_precision") val releaseDatePrecision: String,
    val type: String,
    val artist: List<Artist>? = null
)

@Serializable
data class SeveralAlbumResponse(
    val albums: List<Album>
)