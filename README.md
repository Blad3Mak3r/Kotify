# Kotify ![Maven Central](https://img.shields.io/maven-central/v/tv.blademaker/kotify) ![GitHub release (latest by date)](https://img.shields.io/github/v/release/Blad3Mak3r/Kotify) [![](https://jitpack.io/v/tv.blademaker/Kotify.svg)](https://jitpack.io/#tv.blademaker/Kotify)
Advanced coroutine-based Spotify API wrapper for Kotlin.

## Contens
- [Todo](#todo)
- [Prerequisites](#prerequisites)
- [Getting started](#getting-started)

## TODO
- [x] Rate limits handling.
- [ ] Kotify Http Proxy.
- [ ] Better documentation.

### Authentication
- [x] Client Credentials Flow (``used to authenticate Kotify``).
- [x] Authorization Code Flow (``used by HUGE``).
- [x] Token Refresh (``used by HUGE``).

### Albums
- [x] Get Album. ``Used by HUGE``
- [ ] Get Several Albums. 
- [x] Get Album Tracks. ``Used by HUGE``
- [ ] Get Saved Albums. 
- [ ] Save Albums.
- [ ] Remove Albums.
- [ ] Check Saved Albums.
- [ ] Get New Releases.

### Artists
- [x] Get Artist. ``Used by HUGE``
- [ ] Get Several Artists.
- [ ] Get Artist's Albums.
- [x] Get Artist's Top Tracks. ``Used by HUGE``
- [ ] Get Artist's Related Artists.

### Shows
- [ ] Get Shows.
- [ ] Get Several Shows.
- [ ] Get Show Episodes.
- [ ] Get User's Saved Shows.
- [ ] Save Shows for Current User.
- [ ] Remove User's Saved Shows.
- [ ] Check User's Saved Shows.

### Episodes
- [ ] Get Episode.
- [ ] Get Several Episodes.
- [ ] Get User's Saved Episodes.
- [ ] Save Episodes for User.
- [ ] Remove User's Saved Episodes.
- [ ] Check User's Saved Episodes.

### Tracks
- [x] Get Track. ``Used by HUGE``
- [ ] Get Several Tracks.
- [x] Get User's Saved Tracks. ``Used by HUGE``
- [ ] Save Tracks for Current User.
- [ ] Remove Tracks for Current User.
- [ ] Check User's Saved Tracks.
- [ ] Get Track's Audio Features.
- [ ] Get Track's Audio Analysis.
- [ ] Get Recommendations. ``Used by HUGE``

### Search
- [ ] Search for Item.

### Users:
- [ ] Get Current User's Profile.
- [x] Get User's Top Items. ``Used by HUGE``
- [ ] Get User's Profile
- [ ] Follow Playlist.
- [ ] Unfollow Playlist.
- [ ] Get Followed Artists.
- [ ] Follow Artists or Users.
- [ ] Unfollow Artists or Users.
- [ ] Check If User Follows Artists or Users.
- [ ] Check if Users Follow Playlist.

### Playlists:
- [x] Get Playlist. ``Used by HUGE``
- [ ] Change Playlist Details.
- [x] Get Playlist Items. ``Used by HUGE``
- [ ] Add Items to Playlist.
- [ ] Update Playlist Items.
- [ ] Remove Playlist Items.
- [ ] Get Current User's Playlists. ``Used by HUGE``
- [ ] Get User's Playlist.
- [ ] Create Playlist.
- [ ] Get Featured Playlists. ``Used by HUGE``
- [ ] Get Category's Playlists.
- [ ] Get Playlist Cover Image.
- [ ] Add Custom Playlist Cover Image.

### Categories
- [ ] Get Several Brows Categories.
- [ ] Get Single Brows Category.

### Genres
- [ ] Get Available Genre Seeds.

### Player
- [ ] Get Playback State.
- [ ] Transfer Playback.
- [ ] Get Available Devices.
- [ ] Get Currently Playing Track.
- [ ] Start/Resume Playback.
- [ ] Pause Playback.
- [ ] Skip To Next.
- [ ] Skip To Previous.
- [ ] Seek To Position.
- [ ] Set Repeat Mode.
- [ ] Set Playback Volume.
- [ ] Toggle Playback Shuffle.
- [ ] Get Recently Played Tracks.
- [ ] Add Item to Playback Queue.

### Markets
  - [ ] Get Available Markets.

## Prerequisites
This package has the following requirements:
- [JDK **11**](https://openjdk.java.net/projects/jdk/11/)
- [Kotlin **1.6.20**](https://github.com/JetBrains/kotlin/releases/tag/v1.6.20)
- [Coroutines **1.6.1**](https://github.com/Kotlin/kotlinx.coroutines/releases/tag/1.6.1)
- [Ktor Client **2.0.0**](https://github.com/ktorio/ktor/releases/tag/2.0.0)

## Getting started
Configure the **Kotify** instance and execute requests:
```kotlin
suspend fun main() {

    val kotify = Kotify.withClientCredentials(
        clientId = "Spotify Client ID",
        clientSecret = "Spotify Client Secret"
    )

    // Fetch "This Is Arcane" playlist.
    val playlist: Playlist = kotify.playlists.get("37i9dQZF1DZ06evO30uMeI")
    
    // This will retrieve all the items inside the playlist
    // If the playlist have less than 100 tracks this is not required.
    val items: List<Item> = kotify.playlists.retrieveAllTracks(playlist)
    val tracks: List<Track> = items.map { it.track }
    
    // Fetch "Burn It All Down" track
    val track = kotify.tracks.get("0ked784BOZ1JtRVHux98jE")
}
```

## Download
```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.blad3mak3r:kotify:${VERSION}")
}
```
