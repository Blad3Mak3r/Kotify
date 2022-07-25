package tv.blademaker.kotify

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import tv.blademaker.kotify.internal.CredentialsManager
import tv.blademaker.kotify.services.*
import java.net.URI
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicLong
import kotlin.io.path.toPath

@Suppress("unused")
class Kotify(
    clientID: String,
    clientSecret: String,
    baseUrl: String = "https://api.spotify.com",
    httpClient: OkHttpClient = OkHttpClient()
) {
    private val credentials = CredentialsManager(this, clientID, clientSecret)

    @OptIn(ExperimentalSerializationApi::class)
    internal val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient.newBuilder().addInterceptor(credentials).build())
        .addConverterFactory(JSON.asConverterFactory(contentType))
        .build()

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

    /**
     * Albums service.
     */
    val albums = retrofit.create(AlbumsService::class.java)

    /**
     * Artists service.
     */
    val artists = retrofit.create(ArtistsService::class.java)

    /**
     * Authorization service.
     */
    val authorization = retrofit.create(AuthorizationService::class.java)

    /**
     * Categories service.
     */
    val categories = retrofit.create(CategoriesService::class.java)

    /**
     * Episodes service.
     */
    val episodes = retrofit.create(EpisodesService::class.java)

    /**
     * Playlists service.
     */
    val playlists = retrofit.create(PlaylistsService::class.java)

    /**
     * Search service.
     */
    val search = retrofit.create(SearchService::class.java)

    /**
     * Shows service.
     */
    val shows = retrofit.create(ShowsService::class.java)

    /**
     * Tracks service.
     */
    val tracks = retrofit.create(TracksService::class.java)

    /**
     * User service.
     */
    val user = retrofit.create(UsersService::class.java)

    fun buildAuthorizationCodeFlow(redirectUri: String, scopes: List<Kotify.Scope>, state: String? = null): URL {

        return URL(buildString {
            append("https://accounts.spotify.com/authorize")
            append("?client_id=${credentials.clientId}")
            append("&response_type=code")
            append("&redirect_uri=${URLEncoder.encode(redirectUri, Charset.defaultCharset())}")
            append("&scope=${scopes.parse()}")
            state?.let { append("&state=$it") }
        })
    }

    companion object {
        internal val contentType = "application/json".toMediaType()

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

    enum class Market(
        val value: String
    ) {
        NA("na"),
        ES("es");

        override fun toString(): String {
            return this.value
        }
    }
}