package io.github.blad3mak3r.kotify

import io.github.blad3mak3r.kotify.cache.KotifyCache
import io.github.blad3mak3r.kotify.cache.impl.CaffeineCache
import io.github.blad3mak3r.kotify.internal.CredentialsManager
import io.github.blad3mak3r.kotify.request.Request
import io.github.blad3mak3r.kotify.services.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicLong

@Suppress("unused")
class Kotify private constructor(
    clientID: String,
    clientSecret: String,
    val cache: KotifyCache = CaffeineCache()
) : Closeable {

    internal val credentials = CredentialsManager(this, clientID, clientSecret)

    private val retryAfterRef = AtomicLong(-1L)

    internal var retryAfter: Long
        get() = retryAfterRef.get()
        set(value) {
            log.debug("Adding a retry after of ${value}ms.")
            retryAfterRef.set(System.currentTimeMillis() + value)
        }


    internal val getDelay: Long?
        get() {
            val retryAfter = retryAfterRef.get()

            if (retryAfter > (System.currentTimeMillis()+250))
                return retryAfter - System.currentTimeMillis()

            if (retryAfter != -1L)
                retryAfterRef.set(-1L)

            return null
        }

    @PublishedApi
    internal val httpClient = HttpClient(CIO) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(JSON)
        }
    }

    internal inline fun <reified T : Any> newRequest(method: HttpMethod, path: String, serializer: KSerializer<T>): Request<T> {
        return Request(this, serializer, method, path)
    }

    /**
     * Albums service.
     */
    val albums = AlbumsService(this)

    /**
     * Artists service.
     */
    val artists = ArtistsService(this)

    /**
     * Authorization service.
     */
    val authorization = AuthorizationService(this)

    /**
     * Browse service
     */
    val browse = BrowseService(this)

    /**
     * Categories service.
     */
    val categories = CategoriesService(this)

    /**
     * Episodes service.
     */
    val episodes = EpisodesService(this)

    /**
     * Playlists service.
     */
    val playlists = PlaylistsService(this)

    /**
     * Recommendations service.
     */
    val recommendations = RecommendationsService(this)

    /**
     * Search service.
     */
    val search = SearchService(this)

    /**
     * Shows service.
     */
    val shows = ShowsService(this)

    /**
     * Tracks service.
     */
    val tracks = TracksService(this)

    /**
     * User service.
     */
    val user = UsersService(this)



    private val queue = LinkedList<Request<*>>()

    override fun close() = httpClient.close()

    companion object {
        internal val log = LoggerFactory.getLogger("Kotify")
        internal var baseUrl: String = "https://api.spotify.com"

        var market: String? = null

        const val VERSION = "0.4.6"

        @OptIn(ExperimentalSerializationApi::class)
        val JSON = Json {
            isLenient = true
            ignoreUnknownKeys = true
        }

        fun withClientCredentials(clientId: String, clientSecret: String): Kotify {
            return Kotify(clientId, clientSecret)
        }
    }

    enum class Scope(
        val value: String
    ) {
        // Images
        UGC_IMAGE_UPLOAD("ugc-image-upload"),

        // Spotify Connect
        USER_READ_PLAYBACK_STATE("user-read-playback-state"),
        USER_MODIFY_PLAYBACK_STATE("user-modify-playback-state"),
        USER_READ_CURRENTLY_PLAYING("user-read-currently-playing"),

        // Users
        USER_READ_PRIVATE("user-read-private"),
        USER_READ_EMAIL("user-read-email"),

        // Follow
        USER_FOLLOW_MODIFY("user-follow-modify"),
        USER_FOLLOW_READ("user-follow-read"),

        // Library
        USER_LIBRARY_MODIFY("user-library-modify"),
        USER_LIBRARY_READ("user-library-read"),

        // Playback
        STREAMING("streaming"),
        APP_REMOTE_CONTROL("app-remote-control"),

        // Listening History
        USER_READ_PLAYBACK_POSITION("user-read-playback-position"),
        USER_TOP_READ("user-top-read"),
        USER_READ_RECENTLY_PLAYED("user-read-recently-played"),

        // Playlists
        PLAYLIST_MODIFY_PRIVATE("playlist-modify-private"),
        PLAYLIST_READ_COLLABORATIVE("playlist-read-collaborative"),
        PLAYLIST_READ_PRIVATE("playlist-read-private"),
        PLAYLIST_MODIFY_PUBLIC("playlist-modify-public")
    }
}