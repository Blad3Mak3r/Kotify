package io.github.blad3mak3r.kotify.cache.impl

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.blad3mak3r.kotify.cache.KotifyCache
import io.github.blad3mak3r.kotify.models.*
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


    override suspend fun getAlbum(albumId: String, fallback: suspend () -> Album): Album {
        return albums.getIfPresent(albumId) ?: fallback().also { albums.put(albumId, it) }
    }

    override suspend fun getAlbumTracks(albumId: String, fallback: suspend () -> List<Track>): List<Track> {
        return albumTracks.getIfPresent(albumId) ?: fallback().also { if (it.isNotEmpty()) albumTracks.put(albumId, it) }
    }

    override suspend fun getArtist(artistId: String, fallback: suspend () -> Artist): Artist {
        return artists.getIfPresent(artistId) ?: fallback().also { artists.put(artistId, it) }
    }

    override suspend fun getArtistTopTracks(
        artistId: String,
        fallback: suspend () -> ArtistTopTracks
    ): ArtistTopTracks {
        return artistTopTracks.getIfPresent(artistId) ?: fallback().also { artistTopTracks.put(artistId, it) }
    }

    override suspend fun getPlaylist(playlistId: String, fallback: suspend () -> Playlist): Playlist {
        return playlists.getIfPresent(playlistId) ?: fallback().also { playlists.put(playlistId, it) }
    }

    override suspend fun getPlaylistTracks(playlistId: String, fallback: suspend () -> List<Track>): List<Track> {
        return playlistTracks.getIfPresent(playlistId) ?: fallback().also {
            if (it.isNotEmpty()) playlistTracks.put(playlistId, it)
        }
    }

    override suspend fun getTrack(trackId: String, fallback: suspend () -> Track): Track {
        return tracks.getIfPresent(trackId) ?: fallback().also { tracks.put(trackId, it) }
    }
}