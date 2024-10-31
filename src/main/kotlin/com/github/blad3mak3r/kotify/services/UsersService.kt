package com.github.blad3mak3r.kotify.services

import com.github.blad3mak3r.kotify.Kotify
import com.github.blad3mak3r.kotify.models.User
import com.github.blad3mak3r.kotify.utils.withAccessToken

class UsersService(override val kotify: Kotify) : Service {

    suspend fun getUserTopTracks(accessToken: String): User.TopTracks = withAccessToken(accessToken, this) {
        get("/v1/me/top/tracks", User.TopTracks.serializer()).execute()
    }

    suspend fun getUserTopArtists(accessToken: String): User.TopArtists = withAccessToken(accessToken, this) {
        get("/v1/me/top/artists", User.TopArtists.serializer()).execute()
    }

}