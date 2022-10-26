package tv.blademaker.kotify.services

import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.builtins.ListSerializer
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.*
import tv.blademaker.kotify.models.paginatedRequest
import tv.blademaker.kotify.request.RequestConfiguration
import tv.blademaker.kotify.utils.withAccessToken

class PlaylistsService(override val kotify: Kotify) : Service {

    companion object {
        private val ListSerializer = ListSerializer(Playlist.serializer())
    }

    suspend fun getPlaylist(playlistId: String): Playlist {
        return get("/v1/playlists/$playlistId", Playlist.serializer()).execute()
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

    suspend fun getPlaylistTracks(playlist: Playlist, pages: Int = 6): List<Track> = paginatedRequest(20, 0, pages) { limit, offset ->
        getPlaylistTracksPage(playlist.id, limit, offset)
    }.map { it.track }
}