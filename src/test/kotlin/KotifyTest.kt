import com.github.blad3mak3r.kotify.Kotify
import com.github.blad3mak3r.kotify.exceptions.KotifyException
import com.github.blad3mak3r.kotify.exceptions.KotifyRequestException
import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class KotifyTest {


    private val albumID = "0a70673Gb7q0uqWyoCRy4J"
    private val invalidAlbumID = "0a70673Gb7q0uqWyoCRy4J1234"
    private val notFoundAlbumID = "0a70073Gb7q0uqWyoCRy4J"

    companion object {
        var kotify: Kotify = Kotify(
            clientID = System.getenv("CLIENT_ID"),
            clientSecret = System.getenv("CLIENT_SECRET")
        ).apply {
            Kotify.market = "ES"
        }
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
        val id = /* "2J0TRU2EDG29qlmxdGa4xa" */ "2EoheVFjqIxgJMb8VnDRtZ"

        val totalExpected = 211

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
    fun `5 - Search for playlists`() = runBlocking {
        val result = kotify.search.searchPlaylists("KPOP TRENDS")


        assert(result.playlists.items.isNotEmpty()) {
            "empty recommendations"
        }

        println("Got ${result.playlists.items.size} tracks")
    }

    @Test
    fun `6 - Get new releases`() = runBlocking {
        val newReleases = kotify.browse.getNewReleases(limit = 10).items

        assert(newReleases.isNotEmpty()) {
            "empty releases"
        }

        println("Got ${newReleases.size} new releases")

        val albums = kotify.albums.getSeveralAlbums(*newReleases.map { it.id }.toTypedArray())

        assert(albums.isNotEmpty()) {
            "empty albums"
        }

        println("Got ${albums.size} albums")

        val tracks = albums.map { it.tracks.items }.reduce { acc, tracks -> acc + tracks }

        assert(tracks.isNotEmpty()) {
            "empty tracks"
        }

        println("Got ${tracks.size} tracks")
    }

}