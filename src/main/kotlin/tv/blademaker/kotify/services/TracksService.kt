package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.PlaylistPagination
import tv.blademaker.kotify.models.Track
import tv.blademaker.kotify.request.RequestConfiguration

class TracksService(override val kotify: Kotify) : Service {

    suspend fun get(
        id: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): Track {
        return request(Track.serializer(), {
            path = "/v1/tracks/$id"
        }, configuration)
    }

    suspend fun getUserSavedTracks(
        accessToken: String,
        configuration: RequestConfiguration.() -> Unit = {}
    ): PlaylistPagination {
        return request(PlaylistPagination.serializer(), {
            path = "/v1/me/tracks"
        }, RequestConfiguration().apply(configuration).apply {
            this.accessToken = accessToken
        })
    }
}