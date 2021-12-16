import io.ktor.client.features.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import tv.blademaker.Kotify
import tv.blademaker.exceptions.KotifyRequestException

class KotifyTest {

    private val albumID = "0a70673Gb7q0uqWyoCRy4J"
    private val invalidAlbumID = "0a70673Gb7q0uqWyoCRy4J1234"
    private val notFoundAlbumID = "0a70073Gb7q0uqWyoCRy4J"

    private lateinit var kotify: Kotify

    @Before
    fun setClient() {
        kotify = Kotify(
            clientID = System.getenv("CLIENT_ID"),
            clientSecret = System.getenv("CLIENT_SECRET")
        )
    }

    @Test
    fun `Get Album by ID`() = runBlocking {
        val album = kotify.albums.get(albumID)
        assert(album.id == albumID) { "albumID not equals" }
    }

    @Test
    fun `Get Album with invalid ID`() = runBlocking {
        val result = try {
            kotify.albums.get(invalidAlbumID)
            null
        } catch (e: ClientRequestException) {
            e
        }

        assert(result != null) {
            "Not threw exception"
        }
    }

    @Test
    fun `Get Album with not found ID`() = runBlocking {
        val result = try {
            kotify.albums.get(notFoundAlbumID)
            null
        } catch (e: KotifyRequestException) {
            e
        }

        assert(result != null) {
            "Not threw exception"
        }

        assert(result!!.error.message == "non existing id") {
            result.error.message
        }
    }

    @Test
    fun `Get Album tracks`() = runBlocking {
        val expected = 12
        val tracks = kotify.albums.getTracks(albumID)
        assert(tracks.total == expected) { "not expected value" }
    }

    /*@Test
    fun `Rate limit`(): Unit = runBlocking {
        val number = 500

        val requests = mutableListOf<Deferred<Album>>()

        for (x in 0 until number) {
            println()
            println("Request $x")
            requests.add(kotify.albums.getAsync(albumID))
        }

        awaitAll(*requests.toTypedArray())
    }*/

    @After
    fun close() {
        kotify.close()
    }
}