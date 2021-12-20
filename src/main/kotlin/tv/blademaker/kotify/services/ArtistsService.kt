package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Artist
import tv.blademaker.kotify.models.ArtistTopTracks
import tv.blademaker.kotify.request.RequestConfiguration

class ArtistsService(override val kotify: Kotify) : Service {

    suspend fun get(
        id: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): Artist {
        return request(Artist.serializer(), {
            path = "/v1/artists/$id"
        }, configuration)
    }

    suspend fun getTopTracks(
        id: String,
        market: String = "na",
        configuration: RequestConfiguration.() -> Unit = {}
    ): ArtistTopTracks {
        return request(ArtistTopTracks.serializer(), {
            path = "/v1/artists/$id/top-tracks?market=$market"
        }, configuration)
    }

}