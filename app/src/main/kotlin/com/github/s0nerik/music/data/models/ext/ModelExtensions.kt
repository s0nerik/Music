package com.github.s0nerik.music.data.models.ext

import android.content.ContentUris
import android.net.Uri
import com.github.s0nerik.music.App
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.data.models.Artist
import com.github.s0nerik.music.data.models.Song
import rx.Observable

//region Song extensions

val Song.album: Observable<Album>
    get() = App.getCollectionManager().getAlbum(this)

val Song.artist: Observable<Artist>
    get() = App.getCollectionManager().getArtist(this)

val Song.durationString: String
    get() {
        var seconds = duration / 1000
        val minutes = seconds / 60
        seconds -= minutes * 60
        return "$minutes:${String.format("%02d", seconds)}"
    }

val Song.sourceUri: Uri
    get() = Uri.parse("file://$source")

val Song.albumArtUri: Uri
    get() = ContentUris.withAppendedId(Song.ARTWORK_URI, albumId)

//endregion

//region Artist extensions

val Artist.albums: Observable<List<Album>>
    get() = App.getCollectionManager().getAlbums(this)

//endregion

//region Album extensions

val Album.artist: Observable<Artist>
    get() = App.getCollectionManager().getArtist(this)

val Album.songs: Observable<List<Song>>
    get() = App.getCollectionManager().getSongs(this)

//endregion