package tv.blademaker.kotify.services

interface SearchService{

    /*private suspend fun search(
        type: Type,
        queryBuilder: SearchQueryBuilder.() -> Unit,
        configuration: RequestConfiguration.() -> Unit = {}
    ): SearchResult {

        val query = SearchQueryBuilder().apply(queryBuilder).build()

        return request(SearchResult.serializer(), {
            path = "/v1/search?type=${type.type}&q=$query"
        }, configuration)
    }

    suspend fun searchAlbum(
        query: SearchQueryBuilder.() -> Unit,
        configuration: RequestConfiguration.() -> Unit = {}
    ): AlbumsHolder? = search(Type.ALBUM, query, configuration).albums

    suspend fun searchArtist(
        query: SearchQueryBuilder.() -> Unit,
        configuration: RequestConfiguration.() -> Unit = {}
    ): ArtistsHolder? = search(Type.ARTIST, query, configuration).artists

    suspend fun searchPlaylist(
        query: SearchQueryBuilder.() -> Unit,
        configuration: RequestConfiguration.() -> Unit = {}
    ): PlaylistsHolder? = search(Type.PLAYLIST, query, configuration).playlists

    suspend fun searchTrack(
        query: SearchQueryBuilder.() -> Unit,
        configuration: RequestConfiguration.() -> Unit = {}
    ): TracksHolder? = search(Type.TRACK, query, configuration).tracks

    suspend fun searchShow(
        query: SearchQueryBuilder.() -> Unit,
        configuration: RequestConfiguration.() -> Unit = {}
    ): ShowsHolder? = search(Type.SHOW, query, configuration).shows

    suspend fun searchEpisode(
        query: SearchQueryBuilder.() -> Unit,
        configuration: RequestConfiguration.() -> Unit = {}
    ): EpisodesHolder? = search(Type.EPISODE, query, configuration).episodes
    */

}