package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.TracksPaginator
import tv.blademaker.kotify.models.User
import tv.blademaker.kotify.request.RequestConfiguration

class UsersService(override val kotify: Kotify) : Service {

    suspend fun getTopTracks(configuration: RequestConfiguration.() -> Unit): User.TopTracks {
        return request(User.TopTracks.serializer(), {
            path = "/v1/me/top/tracks"
        }, configuration)
    }

    suspend fun getTopArtists(configuration: RequestConfiguration.() -> Unit): User.TopArtists {
        return request(User.TopArtists.serializer(), {
            path = "/v1/me/top/artists"
        }, configuration)
    }

}