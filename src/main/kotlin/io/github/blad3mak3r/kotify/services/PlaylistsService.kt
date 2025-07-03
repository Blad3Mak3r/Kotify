package io.github.blad3mak3r.kotify.services

import io.github.blad3mak3r.kotify.Kotify
import io.github.blad3mak3r.kotify.models.*
import io.github.blad3mak3r.kotify.utils.withAccessToken
import kotlinx.serialization.builtins.ListSerializer

class PlaylistsService(override val kotify: Kotify) : Service {

    companion object {
        private val ListSerializer = ListSerializer(Playlist.serializer())
    }

    suspend fun getPlaylist(playlistId: String): Playlist {
        return kotify.cache.getPlaylist(playlistId) {
            get("/v1/playlists/$playlistId", Playlist.serializer()).execute()
        }
    }

    suspend fun getCurrentUserPlaylists(accessToken: String): UserPlaylistsPage = withAccessToken(accessToken, this) {
        get("/v1/me/playlists", UserPlaylistsPage.serializer()).execute()
    }

    private suspend fun getPlaylistTracksPage(playlistId: String, limit: Int, offset: Int): PlaylistPagination {
        return get("/v1/playlists/$playlistId/tracks", PlaylistPagination.serializer())
            .limit(limit)
            .offset(offset)
            .execute()
    }

    suspend fun getPlaylistTracks(playlistId: String, pages: Int = 6): List<Track> {
        return kotify.cache.getPlaylistTracks(playlistId) {
            paginatedRequest(50, 0, pages) { limit, offset ->
                getPlaylistTracksPage(playlistId, limit, offset)
            }.map { it.track }
        }
    }
}