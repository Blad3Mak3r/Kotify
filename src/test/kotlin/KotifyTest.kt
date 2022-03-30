import io.ktor.client.plugins.*
import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.exceptions.KotifyRequestException

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class KotifyTest {


    private val albumID = "0a70673Gb7q0uqWyoCRy4J"
    private val invalidAlbumID = "0a70673Gb7q0uqWyoCRy4J1234"
    private val notFoundAlbumID = "0a70073Gb7q0uqWyoCRy4J"

    companion object {
        var kotify: Kotify = Kotify(
            clientID = System.getenv("CLIENT_ID"),
            clientSecret = System.getenv("CLIENT_SECRET")
        )
    }

    @Test
    fun `1 - Get Album by ID`() = runBlocking {
        val album = kotify.albums.get(albumID)
        assert(album.id == albumID) { "albumID not equals" }
    }

    @Test
    fun `2 - Get Album with invalid ID`() = runBlocking {
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
    fun `3 - Get Album with not found ID`() = runBlocking {
        val result = try {
            kotify.albums.get(notFoundAlbumID)
            null
        } catch (e: KotifyRequestException) {
            e
        }

        assert(result != null) {
            "Not threw exception"
        }

        assert(result!!.error.contains("non existing id")) {
            result.error
        }
    }

    @Test
    fun `4 - Get Album tracks`() = runBlocking {
        val expected = 12
        val tracks = kotify.albums.getTracks(albumID)
        assert(tracks.total == expected) { "not expected value" }
    }

    @Test
    fun `5 - Test playlist`() = runBlocking {
        val id = /* "2J0TRU2EDG29qlmxdGa4xa" */ "3ubLYWqCIpRW06a7kRIInC"

        val totalExpected = 2

        val playlist = kotify.playlists.get(id)

        assert(playlist.id == id) {
            "not equal ids"
        }

        val tracks = kotify.playlists.retrieveAllTracks(playlist)

        assert(tracks.size == totalExpected) {
            "received a total of ${tracks.size} but required is $totalExpected"
        }
    }

}