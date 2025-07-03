package io.github.blad3mak3r.kotify.services

import io.github.blad3mak3r.kotify.Kotify
import io.github.blad3mak3r.kotify.models.AlbumsPagination
import io.github.blad3mak3r.kotify.models.NewReleases

class BrowseService(override val kotify: Kotify) : Service {

    suspend fun getNewReleases(limit: Int = 20, offset: Int = 0): AlbumsPagination {
        return get("/v1/browse/new-releases", NewReleases.serializer())
            .limit(limit)
            .offset(offset)
            .execute()
            .albums
    }

}