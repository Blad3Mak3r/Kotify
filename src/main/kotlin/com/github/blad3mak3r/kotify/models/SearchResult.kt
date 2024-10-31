package com.github.blad3mak3r.kotify.models

import kotlinx.serialization.Serializable

@Serializable
data class AlbumsSearchResult(
    val albums: AlbumsPagination
)

@Serializable
data class ArtistsSearchResult(
    val artists: ArtistsPagination
)

@Serializable
data class PlaylistsSearchResult(
    val playlists: PlaylistsPagination
)

@Serializable
data class TracksSearchResult(
    val tracks: TracksPagination
)