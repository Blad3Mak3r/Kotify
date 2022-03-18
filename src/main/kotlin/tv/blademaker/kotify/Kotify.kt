package tv.blademaker.kotify

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import tv.blademaker.kotify.internal.CredentialsManager
import tv.blademaker.kotify.request.Request
import tv.blademaker.kotify.services.*
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

@Suppress("unused")
class Kotify(
    clientID: String,
    clientSecret: String,
    baseUrl: String = "https://api.spotify.com"
) : Closeable {

    internal val credentials = CredentialsManager(this, clientID, clientSecret)

    private val run = AtomicBoolean(true)
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
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    /**
     * Albums service.
     */
    val albums: AlbumsService = Service.of(this)

    /**
     * Artists service.
     */
    val artists: ArtistsService = Service.of(this)

    /**
     * Episodes service.
     */
    val episodes: EpisodesService = Service.of(this)

    /**
     * Playlists service.
     */
    val playlists: PlaylistsService = Service.of(this)

    /**
     * Shows service.
     */
    val shows: ShowsService = Service.of(this)

    /**
     * Tracks service.
     */
    val tracks: TracksService = Service.of(this)

    /**
     * Search service.
     */
    val search: SearchService = Service.of(this)

    /**
     * User service.
     */
    val user: UsersService = Service.of(this)

    private val queue = LinkedList<Request<*>>()

    internal fun enqueue(request: Request<*>) {
        queue.addLast(request)
        log.debug("Added Job to queue: $request")
    }

    internal fun enqueueFirst(request: Request<*>) {
        queue.addFirst(request)
        log.debug("Added Job to queue at first position: $request")
    }

    override fun close() {
        run.set(false)

        while (queue.isNotEmpty()) {
            try {
                queue.poll()?.cancel()
            } catch (_: Exception) { }
        }

        httpClient.close()
    }

    companion object {
        internal val log = LoggerFactory.getLogger("Kotify")
        internal var baseUrl: String = "https://api.spotify.com"
    }
}