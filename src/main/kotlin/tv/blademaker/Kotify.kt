package tv.blademaker

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import tv.blademaker.internal.CredentialsManager
import tv.blademaker.request.Request
import tv.blademaker.services.AlbumService
import java.io.Closeable
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.suspendCoroutine

class Kotify(
    clientID: String,
    clientSecret: String,
    val baseUrl: String = "https://api.spotify.com"
) : Closeable {

    internal val credentials = CredentialsManager(this, clientID, clientSecret)
    internal val encodedCredentials = Base64.getEncoder().encodeToString("$clientID:$clientSecret".toByteArray())

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val queueThread = newSingleThreadContext("kotify-queue-worker")

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val runner = newSingleThreadContext("kotify-runner-worker")
    private val parentJob = SupervisorJob()
    private val run = AtomicBoolean(true)
    internal val retryAfterRef = AtomicLong(-1L)


    private val getDelay: Long
        get() {
            val retryAfter = retryAfterRef.get()

            if (retryAfter > (System.currentTimeMillis()+250))
                return retryAfter - System.currentTimeMillis()

            if (retryAfter != -1L)
                retryAfterRef.set(-1L)

            return 250
        }

    @PublishedApi
    internal val httpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    val albums = AlbumService(this)

    init {
        @Suppress("EXPERIMENTAL_API_USAGE")
        CoroutineScope(queueThread + parentJob).launch {
            runQueue()
        }
    }

    private suspend fun runQueue() {
        while (run.get()) {
            val request = queue.poll() ?: continue

            CoroutineScope(runner).launch {
                request.execute(this@Kotify)
            }

            delay(getDelay)
        }
    }

    private val queue = LinkedBlockingQueue<Request<*>>()

    internal fun enqueue(request: Request<*>) {
        if (!queue.add(request)) error("Cannot add request [${request.method.value}] -> (${request.path}) to queue.")
        println("Added Job to queue: [${request.method.value}] -> (${request.path})")
    }

    override fun close() {
        run.set(false)
        httpClient.close()
    }
}