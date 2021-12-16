package tv.blademaker.services

import io.ktor.client.request.*
import kotlinx.coroutines.Deferred
import kotlinx.serialization.builtins.ListSerializer
import tv.blademaker.Kotify
import tv.blademaker.models.Album
import tv.blademaker.models.TracksPaginator
import tv.blademaker.services.request

class AlbumService(override val kotify: Kotify) : Service {

    suspend fun get(id: String): Album = request {
        path = "/v1/albums/$id"
        serializer = Album.serializer()
    }

    suspend fun getAsync(id: String): Deferred<Album> = requestAsync {
        path = "/v1/albums/$id"
        serializer = Album.serializer()
    }

    suspend fun getSeveral(ids: Collection<String>, market: String? = null): List<Album> = request {
        path = "/v1/albums?ids=${ids.joinToString(",")}" +
                if (market != null) "&=market$market" else ""
        serializer = ListSerializer(Album.serializer())
    }

    suspend fun getTracks(id: String, limit: Int = 20, offset: Int = 0, market: String? = null): TracksPaginator = request {
        path = "/v1/albums/$id/tracks?limit=$limit&offset=$offset" +
                if (market != null) "&=market$market" else ""
        serializer = TracksPaginator.serializer()
    }

    suspend fun getSaved(limit: Int = 20, offset: Int = 0, market: String? = null): List<Album> = request {
        path = "/v1/me/albums?limit=$limit&offset=$offset" +
                if (market != null) "&=market$market" else ""
        serializer = ListSerializer(Album.serializer())
    }
}