import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import tv.blademaker.Kotify

class KotifyTest {

    private val albumID = "0a70673Gb7q0uqWyoCRy4J"

    private val kotify = Kotify(
        clientID = System.getenv("CLIENT_ID"),
        clientSecret = System.getenv("CLIENT_SECRET")
    )

    @Test
    fun `Get Album by ID`() = runBlocking {
        val album = kotify.albums.get(albumID)
        assert(album.id == albumID) { "albumID not equals" }
    }

    @Test
    fun `Get Album tracks`() = runBlocking {
        val expected = 12
        val tracks = kotify.albums.getTracks(albumID)
        assert(tracks.total == expected) { "not expected value" }
    }

    @After
    fun close() {
        kotify.close()
    }
}