import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import tv.blademaker.Kotify
import tv.blademaker.models.Album

class KotifyTest {

    private val albumID = "0a70673Gb7q0uqWyoCRy4J"

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
        Kotify.log.info("Testing Get Album by ID")
        val album = kotify.albums.get(albumID)
        assert(album.id == albumID) { "albumID not equals" }
        Kotify.log.info("Success Album request: $album")
    }

    @Test
    fun `Get Album tracks`() = runBlocking {
        Kotify.log.info("Testing Get Album tracks")
        val expected = 12
        val tracks = kotify.albums.getTracks(albumID)
        assert(tracks.total == expected) { "not expected value" }
        Kotify.log.info("Success Album tracks request: $tracks")
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