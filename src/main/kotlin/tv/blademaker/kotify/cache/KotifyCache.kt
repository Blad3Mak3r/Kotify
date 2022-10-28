package tv.blademaker.kotify.cache

import tv.blademaker.kotify.models.*

interface KotifyCache {

    suspend fun getAlbum(albumId: String): Album?

    suspend fun setAlbum(album: Album)

    suspend fun getAlbumTracks(albumId: String): List<Track>?

    suspend fun setAlbumTracks(albumId: String, tracks: List<Track>)

    suspend fun getArtist(artistId: String): Artist?

    suspend fun setArtist(artist: Artist)

    suspend fun getArtistTopTracks(artistId: String): ArtistTopTracks?

    suspend fun setArtistTopTracks(artistId: String, tracks: ArtistTopTracks)

    suspend fun getPlaylist(playlistId: String): Playlist?

    suspend fun setPlaylist(playlist: Playlist)

    suspend fun getPlaylistTracks(playlistId: String): List<Track>?

    suspend fun setPlaylistTracks(playlistId: String, tracks: List<Track>)

    suspend fun getTrack(trackId: String): Track?

    suspend fun setTrack(track: Track)

}