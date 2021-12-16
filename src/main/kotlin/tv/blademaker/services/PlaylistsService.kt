package tv.blademaker.services

import kotlinx.coroutines.coroutineScope
import tv.blademaker.Kotify
import tv.blademaker.models.Playlist
import tv.blademaker.models.Track
import tv.blademaker.models.TracksPaginator
import java.util.concurrent.atomic.AtomicReference

class PlaylistsService(override val kotify: Kotify) : Service {

    suspend fun get(id: String): Playlist = request {
        path = "/v1/playlists/$id"
        serializer = Playlist.serializer()
    }

    private suspend fun fetchTracksPage(id: String, limit: Int, offset: Int): TracksPaginator = request {
        path = "/v1/playlists/$id/tracks?offset=$offset&limit=$limit"
        serializer = TracksPaginator.serializer()
    }

    suspend fun fetchAllTracks(playlist: Playlist): List<Track> = coroutineScope {
        val tracks = mutableListOf<Track>()
        val paginator = playlist.tracks
        tracks.addAll(paginator.items.filter { !it.isLocal }.map { it.track })

        var nextValues = paginator.nextValues
        while (nextValues != null) {
            val (offset, limit) = nextValues
            val page = fetchTracksPage(playlist.id, limit, offset)
            nextValues = page.nextValues
            tracks.addAll(page.items.filter { !it.isLocal }.map { it.track })
        }

        tracks
    }
}