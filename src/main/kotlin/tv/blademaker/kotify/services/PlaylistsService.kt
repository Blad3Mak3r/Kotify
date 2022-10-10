package tv.blademaker.kotify.services

import kotlinx.coroutines.coroutineScope
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Playlist
import tv.blademaker.kotify.models.PlaylistPagination
import tv.blademaker.kotify.models.Track
import tv.blademaker.kotify.request.RequestConfiguration

class PlaylistsService(override val kotify: Kotify) : Service {

    suspend fun get(
        id: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): Playlist {
        return request(Playlist.serializer(), {
            path = "/v1/playlists/$id"
        }, configuration)
    }

    private suspend fun fetchTracksPage(
        id: String,
        limit: Int,
        offset: Int,
        configuration: RequestConfiguration.() -> Unit = {}
    ): PlaylistPagination {
        return request(PlaylistPagination.serializer(), {
            path = "/v1/playlists/$id/tracks?offset=$offset&limit=$limit"
        }, configuration)
    }

    suspend fun retrieveTracksFromPlaylist(
        playlist: Playlist,
        maxPages: Int = 6,
        configuration: RequestConfiguration.() -> Unit = {}
    ): List<Track> = coroutineScope {
        var currentPage = 1
        val tracks = mutableListOf<Track>()
        val paginator = playlist.tracks
        tracks.addAll(paginator.items.filter { !it.isLocal }.map { it.track })

        var nextValues = paginator.nextValues
        while (nextValues != null && currentPage <=maxPages) {
            currentPage++
            val (offset, limit) = nextValues
            val page = fetchTracksPage(playlist.id, limit, offset, configuration)
            nextValues = page.nextValues
            tracks.addAll(page.items.filter { !it.isLocal }.map { it.track })
        }

        tracks
    }
}