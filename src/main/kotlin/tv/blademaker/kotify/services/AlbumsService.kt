package tv.blademaker.kotify.services

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import tv.blademaker.kotify.models.Album
import tv.blademaker.kotify.models.AlbumPagination

interface AlbumsService {

    @GET("v1/albums/{id}")
    suspend fun getAlbum(@Path("id") id: String): Album

    @GET("v1/albums")
    suspend fun getSeveralAlbums(@Query(encoded = true, value = "ids") ids: String): List<Album>

    @GET("v1/albums/{id}/tracks")
    suspend fun getTracksFromAlbum(@Path("id") id: String, @Query("limit") limit: Int = 20, @Query("offset") offset: Int = 0): AlbumPagination

    @GET("v1/me/albums")
    suspend fun getSavedAlbums(@Header("Authorization") accessToken: String, @Query("limit") limit: Int = 20, @Query("offset") offset: Int = 0): List<Album>
}