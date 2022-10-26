package tv.blademaker.kotify.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.User
import tv.blademaker.kotify.request.Request
import tv.blademaker.kotify.request.RequestConfiguration
import tv.blademaker.kotify.utils.withAccessToken

class UsersService(override val kotify: Kotify) : Service {

    suspend fun getUserTopTracks(accessToken: String): User.TopTracks = withAccessToken(accessToken, this) {
        get("/v1/me/top/tracks", User.TopTracks.serializer()).execute()
    }

    suspend fun getUserTopArtists(accessToken: String): User.TopArtists = withAccessToken(accessToken, this) {
        get("/v1/me/top/artists", User.TopArtists.serializer()).execute()
    }

}