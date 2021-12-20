package tv.blademaker.kotify.services

import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.models.*
import tv.blademaker.kotify.request.RequestConfiguration

class SearchService(override val kotify: Kotify) : Service {

    private enum class Type(val type: String) {
        ALBUM("album"),
        ARTIST("artist"),
        PLAYLIST("playlist"),
        TRACK("track"),
        SHOW("show"),
        EPISODE("episode")
    }

    class SearchQueryBuilder {

        var name: String? = null
        var album: String? = null
        var artist: String? = null
        var track: String? = null
        var year: String? = null
        var upc: String? = null
        var tag: String? = null
        var isrc: String? = null
        var genre: String? = null

        private fun validate() {
            if (name == null) error("Name is empty.")
            if (name == null && album == null && artist == null && track == null && year == null
                && upc == null && tag == null && isrc == null && genre == null) error("Query is empty.")
        }

        fun build(): String {
            validate()
            return buildString {
                append(name)
                album?.let { append("+$album") }
                artist?.let { append("+$artist") }
                track?.let { append("+$track") }
                year?.let { append("+$year") }
                upc?.let { append("+$upc") }
                tag?.let { append("+$tag") }
                isrc?.let { append("+$isrc") }
                genre?.let { append("+$genre") }
            }
        }

    }

    private suspend fun search(
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

}