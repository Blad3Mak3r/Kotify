package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Artist
import tv.blademaker.kotify.models.ArtistTopTracks
import tv.blademaker.kotify.request.RequestConfiguration

class ArtistsService(override val kotify: Kotify) : Service {

    suspend fun getArtist(artistId: String): Artist {
        return get("/v1/artists/$artistId", Artist.serializer()).execute()
    }

    suspend fun getArtistTopTracks(artistId: String, market: String = "na"): ArtistTopTracks {
        return get("/v1/artists/$artistId/top-tracks", ArtistTopTracks.serializer())
            .addQuery("market", market)
            .execute()
    }
}