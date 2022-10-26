package tv.blademaker.kotify.services

import kotlinx.serialization.builtins.ListSerializer
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Album
import tv.blademaker.kotify.models.AlbumPagination
import tv.blademaker.kotify.models.UserAlbumsPage
import tv.blademaker.kotify.models.paginatedRequest
import tv.blademaker.kotify.request.Request
import tv.blademaker.kotify.request.RequestConfiguration
import tv.blademaker.kotify.utils.withAccessToken

class AlbumsService(override val kotify: Kotify) : Service {

    companion object {
        private val AlbumSerializer = Album.serializer()
        private val ListAlbumSerializer = ListSerializer(Album.serializer())
    }

    suspend fun getAlbum(id: String): Album {
        return get("/v1/albums/$id", AlbumSerializer).execute()
    }

    suspend fun getSeveralAlbums(vararg ids: String): List<Album> {
        return get("/v1/albums", ListAlbumSerializer)
            .addQuery("ids", ids.joinToString(","))
            .execute()
    }

    private suspend fun getUserSavedAlbumsPage(limit: Int = 20, offset: Int): UserAlbumsPage {
        return get("/v1/me/albums", UserAlbumsPage.serializer())
            .limit(limit)
            .offset(offset)
            .execute()
    }

    suspend fun getUserSavedAlbums(accessToken: String, pages: Int = 6) = withAccessToken(accessToken, this) {
        paginatedRequest(20, 0, pages) { limit, offset ->
            getUserSavedAlbumsPage(limit, offset)
        }
    }

    private suspend fun getAlbumTracksPage(albumId: String, limit: Int, offset: Int): AlbumPagination {
        return get("/v1/albums/$albumId/tracks", AlbumPagination.serializer())
            .limit(limit)
            .offset(offset)
            .execute()
    }

    suspend fun getAlbumTracks(album: Album, pages: Int = 6) = paginatedRequest(20, 0, pages) { limit, offset ->
        getAlbumTracksPage(album.id, limit, offset)
    }
}