package com.github.blad3mak3r.kotify.services

import com.github.blad3mak3r.kotify.Kotify
import com.github.blad3mak3r.kotify.models.AlbumsSearchResult
import com.github.blad3mak3r.kotify.models.ArtistsSearchResult
import com.github.blad3mak3r.kotify.models.PlaylistsSearchResult
import com.github.blad3mak3r.kotify.models.TracksSearchResult
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class SearchService(override val kotify: Kotify) : Service {

    suspend fun searchTracks(query: String, limit: Int = 50, offset: Int = 0): TracksSearchResult {
        require(query.isNotEmpty()) { "query is empty" }
        require(limit in 1..50) { "not valid limit" }
        require(offset >= 0) { "not valid offset" }
        return get("/v1/search", TracksSearchResult.serializer())
            .addEncodedQuery("q", URLEncoder.encode(query, StandardCharsets.UTF_8))
            .addQuery("type", "track")
            .limit(limit)
            .offset(offset)
            .execute()
    }

    suspend fun searchArtists(query: String, limit: Int = 50, offset: Int = 0): ArtistsSearchResult {
        require(query.isNotEmpty()) { "query is empty" }
        require(limit in 1..50) { "not valid limit" }
        require(offset >= 0) { "not valid offset" }

        return get("/v1/search", ArtistsSearchResult.serializer())
            .addEncodedQuery("q", URLEncoder.encode(query, StandardCharsets.UTF_8))
            .addQuery("type", "artist")
            .limit(limit)
            .offset(offset)
            .execute()
    }

    suspend fun searchPlaylists(query: String, limit: Int = 50, offset: Int = 0): PlaylistsSearchResult {
        require(query.isNotEmpty()) { "query is empty" }
        require(limit in 1..50) { "not valid limit" }
        require(offset >= 0) { "not valid offset" }

        return get("/v1/search", PlaylistsSearchResult.serializer())
            .addEncodedQuery("q", URLEncoder.encode(query, StandardCharsets.UTF_8))
            .addQuery("type", "playlist")
            .limit(limit)
            .offset(offset)
            .execute()
    }

    suspend fun searchAlbums(query: String, limit: Int = 50, offset: Int = 0): AlbumsSearchResult {
        require(query.isNotEmpty()) { "query is empty" }
        require(limit in 1..50) { "not valid limit" }
        require(offset >= 0) { "not valid offset" }

        return get("/v1/search", AlbumsSearchResult.serializer())
            .addEncodedQuery("q", URLEncoder.encode(query, StandardCharsets.UTF_8))
            .addQuery("type", "album")
            .limit(limit)
            .offset(offset)
            .execute()
    }
}