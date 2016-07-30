package com.github.s0nerik.music.ext

import android.content.ContentUris
import android.net.Uri
import com.github.s0nerik.music.App
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.data.models.Artist
import com.github.s0nerik.music.data.models.Song
import rx.Observable

//region Song extensions

val Song.all: Observable<List<Song>>
    get() = App.comp.getCollectionManager().getSongs()

val Song.album: Observable<Album>
    get() = App.comp.getCollectionManager().getAlbum(this)

val Song.artist: Observable<Artist>
    get() = App.comp.getCollectionManager().getArtist(this)

val Song.sourceUri: Uri
    get() = Uri.parse("file://$source")

val Song.albumArtUri: Uri
    get() = ContentUris.withAppendedId(Song.ARTWORK_URI, albumId)

//endregion

//region Artist extensions

val Artist.all: Observable<List<Artist>>
    get() = App.comp.getCollectionManager().getArtists()

val Artist.albums: Observable<List<Album>>
    get() = App.comp.getCollectionManager().getAlbums(this)

//endregion

//region Album extensions

val Album.all: Observable<List<Album>>
    get() = App.comp.getCollectionManager().getAlbums()

val Album.artist: Observable<Artist>
    get() = App.comp.getCollectionManager().getArtist(this)

val Album.songs: Observable<List<Song>>
    get() = App.comp.getCollectionManager().getSongs(this)

//endregion