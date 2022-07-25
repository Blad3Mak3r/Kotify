package tv.blademaker.kotify.services

import kotlinx.coroutines.coroutineScope
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tv.blademaker.kotify.models.Playlist
import tv.blademaker.kotify.models.PlaylistPagination
import tv.blademaker.kotify.models.Track

interface PlaylistsService {

    @GET("v1/playlists/{id}")
    suspend fun getPlaylist(@Path("id") id: String): Playlist

    @GET("v1/playlists/{id}/tracks")
    suspend fun getPlaylistPaginationPage(@Path("id") id: String, @Query("limit") limit: Int, @Query("offset") offset: Int): PlaylistPagination

    suspend fun retrieveAllTracks(
        playlist: Playlist,
        maxPages: Int = Int.MAX_VALUE
    ): List<Track> = coroutineScope {
        val tracks = mutableListOf<Track>()
        val paginator = playlist.tracks
        tracks.addAll(paginator.items.filter { !it.isLocal }.map { it.track })
        var currentPage = 0

        var nextValues = paginator.nextValues
        while (nextValues != null && maxPages <= currentPage) {
            val (offset, limit) = nextValues
            val page = getPlaylistPaginationPage(playlist.id, limit, offset)
            currentPage++
            nextValues = page.nextValues
            tracks.addAll(page.items.filter { !it.isLocal }.map { it.track })
        }

        tracks
    }

    /*suspend fun get(
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

    suspend fun retrieveAllTracks(
        playlist: Playlist,
        configuration: RequestConfiguration.() -> Unit = {}
    ): List<Track> = coroutineScope {
        val tracks = mutableListOf<Track>()
        val paginator = playlist.tracks
        tracks.addAll(paginator.items.filter { !it.isLocal }.map { it.track })

        var nextValues = paginator.nextValues
        while (nextValues != null) {
            val (offset, limit) = nextValues
            val page = fetchTracksPage(playlist.id, limit, offset, configuration)
            nextValues = page.nextValues
            tracks.addAll(page.items.filter { !it.isLocal }.map { it.track })
        }

        tracks
    }*/
}