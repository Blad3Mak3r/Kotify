package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.Recommendations
import tv.blademaker.kotify.request.RequestConfiguration

class RecommendationsService(override val kotify: Kotify) : Service {

    suspend fun byTracksId(
        ids: Collection<String>,
        configuration: RequestConfiguration.() -> Unit = {}
    ): Recommendations {
        require(ids.isNotEmpty()) { "list is empty" }
        require(ids.size <= 5) { "list must not contain more than 5 ids" }
        return request(Recommendations.serializer(), {
            path = "/v1/recommendations"
            query["seed_tracks"] = ids.joinToString(",")
        }, RequestConfiguration().apply(configuration))
    }

}