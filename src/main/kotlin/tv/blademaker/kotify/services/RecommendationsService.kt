package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Recommendations

class RecommendationsService(override val kotify: Kotify) : Service {

    suspend fun byTrackIds(vararg ids: String): Recommendations {
        require(ids.isNotEmpty()) { "ids is empty" }
        require(ids.size <= 5) { "ids must not contain more than 5 ids" }
        return get("/v1/recommendations", Recommendations.serializer())
            .addQuery("seed_tracks", ids.joinToString(","))
            .execute()
    }

}