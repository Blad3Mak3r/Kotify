package tv.blademaker.services

import tv.blademaker.Kotify
import tv.blademaker.models.Track

class TracksService(override val kotify: Kotify) : Service {

    suspend fun get(id: String): Track {
        return request(Track.serializer()) {
            path = "/v1/tracks/$id"
        }
    }
}