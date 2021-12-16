import io.ktor.client.features.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import tv.blademaker.Kotify
import tv.blademaker.exceptions.KotifyRequestException
import tv.blademaker.models.Album
import tv.blademaker.models.Playlist

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

    @Test
    fun `Test playlist`() = runBlocking {
        val id = /* "2J0TRU2EDG29qlmxdGa4xa" */ "3bN65vmek1MvTlmAYTqNQ9"

        val totalExpected = 430

        val playlist = kotify.playlists.get(id)

        assert(playlist.id == id) {
            "not equal ids"
        }

        val tracks = kotify.playlists.fetchAllTracks(playlist)

        assert(tracks.size == totalExpected) {
            "received a total of ${tracks.size} but required is $totalExpected"
        }
    }

    @After
    fun close() {
        kotify.close()
    }

}