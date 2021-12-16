package tv.blademaker.services

import tv.blademaker.Kotify
import tv.blademaker.models.Artist
import tv.blademaker.models.ArtistTopTracks

class ArtistsService(override val kotify: Kotify) : Service {

    suspend fun get(id: String): Artist = request {
        path = "/v1/artists/$id"
        serializer = Artist.serializer()
    }

    suspend fun getTopTracks(id: String, market: String = "na"): ArtistTopTracks = request {
        path = "/v1/artists/$id/top-tracks?market=$market"
        serializer = ArtistTopTracks.serializer()
    }

}