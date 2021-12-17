package tv.blademaker.services

import kotlinx.coroutines.Deferred
import kotlinx.serialization.builtins.ListSerializer
import tv.blademaker.Kotify
import tv.blademaker.models.Album
import tv.blademaker.models.TracksPaginator

class AlbumsService(override val kotify: Kotify) : Service {

    suspend fun get(id: String): Album {
        return request(Album.serializer()) {
            path = "/v1/albums/$id"
        }
    }

    suspend fun getAsync(id: String): Deferred<Album> {
        return requestAsync(Album.serializer()) {
            path = "/v1/albums/$id"
        }
    }

    suspend fun getSeveral(ids: Collection<String>, market: String? = null): List<Album> {
        return request(ListSerializer(Album.serializer())) {
            path = "/v1/albums?ids=${ids.joinToString(",")}" + if (market != null) "&=market$market" else ""
        }
    }

    suspend fun getTracks(id: String, limit: Int = 20, offset: Int = 0, market: String? = null): TracksPaginator {
        return request(TracksPaginator.serializer()) {
            path = "/v1/albums/$id/tracks?limit=$limit&offset=$offset" + if (market != null) "&=market$market" else ""
        }
    }

    suspend fun getSaved(limit: Int = 20, offset: Int = 0, market: String? = null): List<Album> {
        return request(ListSerializer(Album.serializer())) {
            path = "/v1/me/albums?limit=$limit&offset=$offset" + if (market != null) "&=market$market" else ""
        }
    }
}