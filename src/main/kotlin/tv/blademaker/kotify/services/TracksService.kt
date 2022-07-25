package tv.blademaker.kotify.services

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import tv.blademaker.kotify.models.PlaylistPagination
import tv.blademaker.kotify.models.SeveralTracksPage
import tv.blademaker.kotify.models.Track

interface TracksService {

    @GET("v1/tracks/{trackId}")
    suspend fun getTrack(@Path("trackId") trackId: String): Track

    @GET("v1/tracks")
    suspend fun getSeveralTracks(@Query(encoded = true, value = "ids") ids: String): SeveralTracksPage

    @GET("v1/me/tracks")
    suspend fun getUserSavedTracks(@Header("Authorization") accessToken: String, @Query("limit") limit: Int = 50, @Query("offset") offset: Int = 0): PlaylistPagination

    /*suspend fun get(
        id: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): Track {
        return request(Track.serializer(), {
            path = "/v1/tracks/$id"
        }, configuration)
    }

    suspend fun getSeveral(
        vararg ids: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): SeveralTracksPage {
        check(ids.size in 1..50) {
            "Provided ids must be between 1 and 50"
        }

        return request(SeveralTracksPage.serializer(), {
            path = "/v1/tracks?ids=${ids.joinToString(",")}"
        }, configuration)
    }

    suspend fun getUserSavedTracks(
        accessToken: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): PlaylistPagination {
        return request(PlaylistPagination.serializer(), {
            path = "/v1/me/tracks?limit=50"
        }, RequestConfiguration().apply(configuration).apply {
            this.accessToken = accessToken
        })
    }*/
}