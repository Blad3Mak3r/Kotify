package com.github.blad3mak3r.kotify.services

import com.github.blad3mak3r.kotify.Kotify
import com.github.blad3mak3r.kotify.models.Artist
import com.github.blad3mak3r.kotify.models.ArtistTopTracks

class ArtistsService(override val kotify: Kotify) : Service {

    suspend fun getArtist(artistId: String): Artist {
        return kotify.cache.getArtist(artistId) {
            get("/v1/artists/$artistId", Artist.serializer()).execute()
        }
    }

    suspend fun getArtistTopTracks(artistId: String, market: String = "na"): ArtistTopTracks {
        return kotify.cache.getArtistTopTracks(artistId) {
            get("/v1/artists/$artistId/top-tracks", ArtistTopTracks.serializer())
                .addQuery("market", market)
                .execute()
        }
    }
}