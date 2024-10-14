import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import tv.blademaker.kotify.Kotify
import tv.blademaker.kotify.exceptions.KotifyException
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
        val album = kotify.albums.getAlbum(albumID)
        assert(album.id == albumID) { "albumID not equals" }
    }

    @Test
    fun `2 - Get Album with invalid ID`() = runBlocking {
        val result = try {
            kotify.albums.getAlbum(invalidAlbumID)
            null
        } catch (e: KotifyRequestException) {
            e
        } catch (e: KotifyException) {
            e
        }

        assert(result != null) {
            "Not threw exception"
        }
    }

    @Test
    fun `3 - Get Album tracks`() = runBlocking {
        val expected = 12
        val tracks = kotify.albums.getAlbumTracks(albumID)
        assert(tracks.size == expected) { "not expected value" }
    }

    @Test
    fun `4 - Test playlist`() = runBlocking {
        val id = /* "2J0TRU2EDG29qlmxdGa4xa" */ "3ubLYWqCIpRW06a7kRIInC"

        val totalExpected = 2

        val playlist = kotify.playlists.getPlaylist(id)

        assert(playlist.id == id) {
            "not equal ids"
        }

        val tracks = kotify.playlists.getPlaylistTracks(playlist.id)

        assert(tracks.size == totalExpected) {
            "received a total of ${tracks.size} but required is $totalExpected"
        }
    }

    @Test
    fun `5 - Get recommendations by track id`() = runBlocking {
        val recommendations = kotify.recommendations.byTrackIds("03UrZgTINDqvnUMbbIMhql")

        val tracks = recommendations.tracks

        assert(tracks.isNotEmpty()) {
            "empty recommendations"
        }

        println("Got ${tracks.size} tracks")
    }

}