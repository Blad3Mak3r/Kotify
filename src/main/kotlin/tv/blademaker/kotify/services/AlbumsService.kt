package tv.blademaker.kotify.services

import kotlinx.coroutines.Deferred
import kotlinx.serialization.builtins.ListSerializer
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Album
import tv.blademaker.kotify.models.AlbumPagination
import tv.blademaker.kotify.request.RequestConfiguration

class AlbumsService(override val kotify: Kotify) : Service {

    suspend fun get(
        id: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): Album {
        return request(Album.serializer(), {
            path = "/v1/albums/$id"
        }, configuration)
    }

    suspend fun getAsync(
        id: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): Deferred<Album> {
        return requestAsync(Album.serializer(), {
            path = "/v1/albums/$id"
        }, configuration)
    }

    suspend fun getSeveral(
        ids: Collection<String>,
        market: String? = null,
        configuration: RequestConfiguration.() -> Unit = {}
    ): List<Album> {
        return request(ListSerializer(Album.serializer()), {
            path = "/v1/albums?ids=${ids.joinToString(",")}" + if (market != null) "&=market$market" else ""
        }, configuration)
    }

    suspend fun getTracks(
        id: String,
        limit: Int = 20,
        offset: Int = 0,
        market: String? = null,
        configuration: RequestConfiguration.() -> Unit = {}
    ): AlbumPagination {
        return request(AlbumPagination.serializer(), {
            path = "/v1/albums/$id/tracks?limit=$limit&offset=$offset" + if (market != null) "&=market$market" else ""
        }, configuration)
    }

    suspend fun getSaved(
        limit: Int = 20,
        offset: Int = 0,
        market: String? = null,
        configuration: RequestConfiguration.() -> Unit = {}
    ): List<Album> {
        return request(ListSerializer(Album.serializer()), {
            path = "/v1/me/albums?limit=$limit&offset=$offset" + if (market != null) "&=market$market" else ""
        }, configuration)
    }
}