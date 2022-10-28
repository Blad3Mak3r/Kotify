package tv.blademaker.kotify.cache

import tv.blademaker.kotify.models.*

interface KotifyCache {

    suspend fun getAlbum(albumId: String, fallback: suspend () -> Album): Album

    suspend fun getAlbumTracks(albumId: String, fallback: suspend () -> List<Track>): List<Track>

    suspend fun getArtist(artistId: String, fallback: suspend () -> Artist): Artist

    suspend fun getArtistTopTracks(artistId: String, fallback: suspend () -> ArtistTopTracks): ArtistTopTracks

    suspend fun getPlaylist(playlistId: String, fallback: suspend () -> Playlist): Playlist

    suspend fun getPlaylistTracks(playlistId: String, fallback: suspend () -> List<Track>): List<Track>

    suspend fun getTrack(trackId: String, fallback: suspend () -> Track): Track

}