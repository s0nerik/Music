package com.github.s0nerik.music.data.models

import com.fernandocejas.frodo.annotation.RxLogObservable
import com.github.s0nerik.music.data.helpers.db.CursorConstructor
import com.github.s0nerik.music.data.helpers.db.cursor_getters.AlbumsCursorGetter
import com.github.s0nerik.music.data.helpers.db.cursor_getters.ArtistsCursorGetter
import com.github.s0nerik.music.data.helpers.db.cursor_getters.SongsCursorGetter
import ru.noties.debug.Debug
import rx.Observable
import java.io.Serializable

class MusicCollectionImpl : MusicCollection, Serializable {
    val songs: MutableMap<Long, Song> = mutableMapOf()
    val albums: MutableMap<Long, Album> = mutableMapOf()
    val artists: MutableMap<Long, Artist> = mutableMapOf()

    override fun initFromMediaStore(): Observable<MusicCollection> {
        return loadAllSongs()
                .doOnCompleted { Debug.d("Songs loaded...") }
                .flatMap { loadAllArtists() }
                .doOnCompleted { Debug.d("Artists loaded...") }
                .flatMap { loadAllAlbums() }
                .doOnCompleted { Debug.d("Albums loaded...") }
//                      .concatWith(writeCollectionIntoFile())
//                      .doOnCompleted { Debug.d("Collection written to file.") }
                .flatMap { Observable.just(this) }
    }

    override fun getArtists(): Observable<List<Artist>> {
        return Observable.just(artists.map { it.value })
    }

    override fun getArtist(song: Song): Observable<Artist> {
        return Observable.just(artists[song.artistId])
    }

    override fun getAlbums(): Observable<List<Album>> {
        return Observable.just(albums.map { it.value })
    }

    override fun getAlbum(song: Song): Observable<Album> {
        return Observable.just(albums[song.albumId])
    }

    override fun getArtist(album: Album): Observable<Artist> {
        return Observable.just(artists[album.artistId])
    }

    override fun getSongs(): Observable<List<Song>> {
        return Observable.just(songs.map { it.value })
    }

    override fun getSongs(album: Album): Observable<List<Song>> {
        return Observable.just(songs.filterValues { it.albumId == album.id }.map { it.value })
    }

    override fun getSongs(artist: Artist): Observable<List<Song>> {
        return Observable.just(songs.filterValues { it.artistId == artist.id }.map { it.value })
    }

    override fun getAlbums(artist: Artist): Observable<List<Album>> {
        return Observable.just(albums.filterValues { it.artistId == artist.id }.map { it.value })
    }

    @RxLogObservable
    private fun loadAllSongs(): Observable<List<Song>> {
        return CursorConstructor.fromCursorGetter(Song.Factory(), SongsCursorGetter())
                .doOnNext { songs[it.id] = it }
                .toList()
    }

    private fun loadAllArtists(): Observable<List<Artist>> {
        return CursorConstructor.fromCursorGetter(Artist.Factory(), ArtistsCursorGetter())
                .onBackpressureBuffer()
                .filter { isArtistHasSongs(it) }
                .doOnNext { artists[it.id] = it }
                .toList()
    }

    private fun loadAllAlbums(): Observable<List<Album>> {
        return CursorConstructor.fromCursorGetter(Album.Factory(), AlbumsCursorGetter())
                .onBackpressureBuffer()
                .filter { isAlbumContainsSongs(it) }
                .doOnNext { albums[it.id] = it }
                .toList()
    }

    private fun isArtistHasSongs(artist: Artist): Boolean {
        return songs.any { it.value.artistId == artist.id }
    }

    private fun isAlbumContainsSongs(album: Album): Boolean {
        return songs.any { it.value.albumId == album.id }
    }
}