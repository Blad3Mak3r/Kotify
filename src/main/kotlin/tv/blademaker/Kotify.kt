package tv.blademaker

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import tv.blademaker.internal.CredentialsManager
import tv.blademaker.request.Request
import tv.blademaker.services.*
import java.io.Closeable
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class Kotify(
    clientID: String,
    clientSecret: String,
    baseUrl: String = "https://api.spotify.com"
) : Closeable {

    internal val credentials = CredentialsManager(this, clientID, clientSecret)

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val queueThread = newSingleThreadContext("kotify-queue-worker")

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val runner = newSingleThreadContext("kotify-runner-worker")
    private val parentJob = SupervisorJob()
    private val run = AtomicBoolean(true)
    private val retryAfterRef = AtomicLong(-1L)

    internal var retryAfter: Long
        get() = retryAfterRef.get()
        set(value) {
            log.debug("Adding a retry after of ${value}ms.")
            retryAfterRef.set(System.currentTimeMillis() + value)
        }

    init {
        Kotify.baseUrl = baseUrl
    }


    private val getDelay: Long
        get() {
            val retryAfter = retryAfterRef.get()

            if (retryAfter > (System.currentTimeMillis()+250))
                return retryAfter - System.currentTimeMillis()

            if (retryAfter != -1L)
                retryAfterRef.set(-1L)

            return 0L
        }

    @PublishedApi
    internal val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    val albums: AlbumsService = Service.of(this)
    val artists: ArtistsService = Service.of(this)
    val episodes: EpisodesService = Service.of(this)
    val playlists: PlaylistsService = Service.of(this)
    val shows: ShowsService = Service.of(this)
    val tracks: TracksService = Service.of(this)

    init {
        @Suppress("EXPERIMENTAL_API_USAGE")
        CoroutineScope(queueThread + parentJob).launch {
            runQueue()
        }
    }

    private suspend fun runQueue() = coroutineScope {
        while (run.get()) {
            val request = queue.poll() ?: continue

            log.debug("Executing request $request")
            val done = request.execute(this@Kotify)
            if (done) log.debug("Finished request $request")

            val delayMs = getDelay
            if (delayMs >=1L) log.debug("Waiting for ${delayMs}ms to execute next request.")
            delay(delayMs)
        }
    }

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