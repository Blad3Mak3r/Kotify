package tv.blademaker.kotify.cache.impl

import com.github.benmanes.caffeine.cache.Caffeine
import tv.blademaker.kotify.cache.KotifyCache
import tv.blademaker.kotify.models.*
import java.time.Duration

class CaffeineCache : KotifyCache {

    private val albums = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(12))
        .build<String, Album>()

    private val albumTracks = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(12))
        .build<String, List<Track>>()

    private val artists = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(12))
        .build<String, Artist>()

    private val artistTopTracks = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(6))
        .build<String, ArtistTopTracks>()

    private val playlists = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(30))
        .build<String, Playlist>()

    private val playlistTracks = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(30))
        .build<String, List<Track>>()

    private val tracks = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofHours(2))
        .build<String, Track>()

    override suspend fun getAlbum(albumId: String): Album? {
        return albums.getIfPresent(albumId)
    }

    override suspend fun setAlbum(album: Album) {
        return albums.put(album.id, album)
    }

    override suspend fun getAlbumTracks(albumId: String): List<Track>? {
        return albumTracks.getIfPresent(albumId)
    }

    override suspend fun setAlbumTracks(albumId: String, tracks: List<Track>) {
        return albumTracks.put(albumId, tracks)
    }

    override suspend fun getArtist(artistId: String): Artist? {
        return artists.getIfPresent(artistId)
    }

    override suspend fun setArtist(artist: Artist) {
        return artists.put(artist.id, artist)
    }

    override suspend fun getArtistTopTracks(artistId: String): ArtistTopTracks? {
        return artistTopTracks.getIfPresent(artistId)
    }

    override suspend fun setArtistTopTracks(artistId: String, tracks: ArtistTopTracks) {
        return artistTopTracks.put(artistId, tracks)
    }

    override suspend fun getPlaylist(playlistId: String): Playlist? {
        return playlists.getIfPresent(playlistId)
    }

    override suspend fun setPlaylist(playlist: Playlist) {
        return playlists.put(playlist.id, playlist)
    }

    override suspend fun getPlaylistTracks(playlistId: String): List<Track>? {
        return playlistTracks.getIfPresent(playlistId)
    }

    override suspend fun setPlaylistTracks(playlistId: String, tracks: List<Track>) {
        return playlistTracks.put(playlistId, tracks)
    }

    override suspend fun getTrack(trackId: String): Track? {
        return tracks.getIfPresent(trackId)
    }

    override suspend fun setTrack(track: Track) {
        return tracks.put(track.id, track)
    }
}