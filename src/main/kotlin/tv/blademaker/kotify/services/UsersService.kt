package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.User
import tv.blademaker.kotify.request.RequestConfiguration

class UsersService(override val kotify: Kotify) : Service {

    suspend fun getTopTracks(accessToken: String, configuration: RequestConfiguration.() -> Unit = {}): User.TopTracks {
        return request(User.TopTracks.serializer(), {
            path = "/v1/me/top/tracks"
        }, RequestConfiguration().apply(configuration).apply {
            this.accessToken = accessToken
        })
    }

    suspend fun getTopArtists(accessToken: String, configuration: RequestConfiguration.() -> Unit = {}): User.TopArtists {
        return request(User.TopArtists.serializer(), {
            path = "/v1/me/top/artists"
        }, RequestConfiguration().apply(configuration).apply {
            this.accessToken = accessToken
        })
    }

}