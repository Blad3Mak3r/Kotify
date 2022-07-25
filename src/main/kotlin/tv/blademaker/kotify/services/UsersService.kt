package tv.blademaker.kotify.services

import retrofit2.http.GET
import retrofit2.http.Header
import tv.blademaker.kotify.models.User

interface UsersService {

    @GET("v1/me/top/tracks")
    suspend fun getTopTracks(@Header("Authorization") accessToken: String): User.TopTracks

    @GET("v1/me/top/artists")
    suspend fun getTopArtists(@Header("Authorization") accessToken: String): User.TopArtists

}