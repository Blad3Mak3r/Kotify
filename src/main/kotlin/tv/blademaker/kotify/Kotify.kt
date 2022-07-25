package tv.blademaker.kotify

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import tv.blademaker.kotify.internal.CredentialsManager
import tv.blademaker.kotify.models.Playlist
import tv.blademaker.kotify.models.PlaylistPagination
import tv.blademaker.kotify.models.Track
import tv.blademaker.kotify.request.Request
import tv.blademaker.kotify.request.RequestConfiguration
import tv.blademaker.kotify.services.*
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicLong

@Suppress("unused")
class Kotify(
    clientID: String,
    clientSecret: String,
    baseUrl: String = "https://api.spotify.com"
) : Closeable {

    internal val credentials = CredentialsManager(this, clientID, clientSecret)

    private val retryAfterRef = AtomicLong(-1L)

    internal var retryAfter: Long
        get() = retryAfterRef.get()
        set(value) {
            log.debug("Adding a retry after of ${value}ms.")
            retryAfterRef.set(System.currentTimeMillis() + value)
        }

    init {
        Companion.baseUrl = baseUrl
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

        const val VERSION = "0.4.6"

        val JSON = Json {
            isLenient = true
            ignoreUnknownKeys = true
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