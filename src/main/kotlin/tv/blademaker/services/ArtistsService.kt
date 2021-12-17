package tv.blademaker.services

import tv.blademaker.Kotify
import tv.blademaker.models.Artist
import tv.blademaker.models.ArtistTopTracks

class ArtistsService(override val kotify: Kotify) : Service {

    suspend fun get(id: String): Artist {
        return request(Artist.serializer()) {
            path = "/v1/artists/$id"
        }
    }

    suspend fun getTopTracks(id: String, market: String = "na"): ArtistTopTracks {
        return request(ArtistTopTracks.serializer()) {
            path = "/v1/artists/$id/top-tracks?market=$market"
        }
    }

}