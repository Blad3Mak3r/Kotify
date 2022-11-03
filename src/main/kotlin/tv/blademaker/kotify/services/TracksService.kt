package tv.blademaker.kotify.services

import io.ktor.http.*
import kotlinx.serialization.builtins.serializer
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.PlaylistPagination
import tv.blademaker.kotify.models.SeveralTracksPage
import tv.blademaker.kotify.models.Track
import tv.blademaker.kotify.models.paginatedRequest
import tv.blademaker.kotify.utils.withAccessToken

class TracksService(override val kotify: Kotify) : Service {

    suspend fun getTrack(trackId: String): Track {
        return kotify.cache.getTrack(trackId) {
            get("/v1/tracks/$trackId", Track.serializer()).execute()
        }
    }

    suspend fun getSeveralTrack(vararg ids: String): SeveralTracksPage {
        check(ids.size in 1..50) {
            "ids must be higher than 0 or less than 50"
        }
        return get("/v1/tracks", SeveralTracksPage.serializer())
            .addQuery("ids", ids.joinToString(","))
            .execute()
    }

    private suspend fun getUserSavedTracksPage(accessToken: String, limit: Int = 50, offset: Int = 0): PlaylistPagination = withAccessToken(accessToken, this) {
        get("/v1/me/tracks", PlaylistPagination.serializer())
            .limit(limit)
            .offset(offset)
            .execute()
    }

    suspend fun getUserSavedTracks(accessToken: String, pages: Int = 6) = withAccessToken(accessToken, this) {
        paginatedRequest(50, 0, pages) { limit, offset ->
            getUserSavedTracksPage(accessToken, limit, offset)
        }.map { it.track }
    }

    suspend fun saveTrackForCurrentUser(accessToken: String, vararg trackId: String) = withAccessToken(accessToken, this) {
        kotify.newRequest(HttpMethod.Put, "/v1/me/tracks", Unit.serializer())
            .addQuery("ids", trackId.joinToString(","))
            .execute()
    }
}