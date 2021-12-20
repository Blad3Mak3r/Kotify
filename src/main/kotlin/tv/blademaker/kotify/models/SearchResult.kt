package tv.blademaker.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val tracks: TracksHolder? = null,
    val artists: ArtistsHolder? = null,
    val albums: AlbumsHolder? = null,
    val playlists: PlaylistsHolder? = null,
    val shows: ShowsHolder? = null,
    val episodes: EpisodesHolder? = null
)

@Serializable
data class TracksHolder(
    override val href: String,
    override val items: List<Track>,
    override val limit: Int,
    override val offset: Int,
    override val next: String? = null,
    override val previous: String? = null,
    override val total: Int
) : SearchHolder<Track>

@Serializable
data class ArtistsHolder(
    override val href: String,
    override val items: List<Artist>,
    override val limit: Int,
    override val offset: Int,
    override val next: String? = null,
    override val previous: String? = null,
    override val total: Int
) : SearchHolder<Artist>

@Serializable
data class AlbumsHolder(
    override val href: String,
    override val items: List<Album>,
    override val limit: Int,
    override val offset: Int,
    override val next: String? = null,
    override val previous: String? = null,
    override val total: Int
) : SearchHolder<Album>

@Serializable
data class PlaylistsHolder(
    override val href: String,
    override val items: List<Playlist>,
    override val limit: Int,
    override val offset: Int,
    override val next: String? = null,
    override val previous: String? = null,
    override val total: Int
) : SearchHolder<Playlist>

@Serializable
data class ShowsHolder(
    override val href: String,
    override val items: List<Show>,
    override val limit: Int,
    override val offset: Int,
    override val next: String? = null,
    override val previous: String? = null,
    override val total: Int
) : SearchHolder<Show>

@Serializable
data class EpisodesHolder(
    override val href: String,
    override val items: List<Episode>,
    override val limit: Int,
    override val offset: Int,
    override val next: String? = null,
    override val previous: String? = null,
    override val total: Int
) : SearchHolder<Episode>

interface SearchHolder<T> {
    val href: String
    val items: List<T>
    val limit: Int
    val offset: Int
    val next: String?
    val previous: String?
    val total: Int
}