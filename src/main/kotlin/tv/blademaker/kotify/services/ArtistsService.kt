package tv.blademaker.kotify.services

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Artist
import tv.blademaker.kotify.models.ArtistTopTracks

interface ArtistsService {

    @GET("v1/artists/{id}")
    suspend fun getArtist(@Path("id") id: String): Artist

    @GET("v1/artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(@Path("id") id: String, @Query("market") market: Kotify.Market = Kotify.Market.NA): ArtistTopTracks

}