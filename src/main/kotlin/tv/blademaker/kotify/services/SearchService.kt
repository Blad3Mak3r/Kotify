package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.TracksSearchResult
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SearchService(override val kotify: Kotify) : Service {

    suspend fun searchTracks(query: String, limit: Int = 50, offset: Int = 0): TracksSearchResult {
        require(query.isNotEmpty()) { "query is empty" }
        require(limit in 1..50) { "not valid limit" }
        require(offset >= 0) { "not valid offset" }
        return get("/v1/search", TracksSearchResult.serializer())
            .addQuery("q", URLEncoder.encode(query, StandardCharsets.UTF_8))
            .addQuery("type", "track")
            .limit(limit)
            .offset(offset)
            .execute()
    }
}