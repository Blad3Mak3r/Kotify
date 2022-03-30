package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.*
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
            path = "/v1/me/tracks"
        }, RequestConfiguration().apply(configuration).apply {
            this.accessToken = accessToken
        })
    }
}