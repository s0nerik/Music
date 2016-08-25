package com.github.s0nerik.music.ext

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.data.models.Artist
import com.github.s0nerik.music.data.models.Song
import rx.Observable
import java.io.File

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

fun Song.setAsRingtone(context: Context) {
    val newRingtone = File(source)

    val values = ContentValues()
    values.put(MediaStore.MediaColumns.DATA, newRingtone.absolutePath)
    values.put(MediaStore.MediaColumns.SIZE, newRingtone.length())
    values.put(MediaStore.MediaColumns.TITLE, title)
    values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
    values.put(MediaStore.Audio.Media.DURATION, duration)
    values.put(MediaStore.Audio.Media.ARTIST, artistName)
    values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
    values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
    values.put(MediaStore.Audio.Media.IS_ALARM, false)
    values.put(MediaStore.Audio.Media.IS_MUSIC, false)

    val uri = MediaStore.Audio.Media.getContentUriForPath(newRingtone.absolutePath)
    context.contentResolver.delete(uri, "${MediaStore.MediaColumns.DATA} = \"${newRingtone.absolutePath}\"", null)
    val newUri = context.contentResolver.insert(uri, values)

    try {
        RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri)
        Toast.makeText(context, String.format(context.getString(R.string.format_ringtone), title), Toast.LENGTH_LONG).show()
    } catch (t: Throwable) {

    }
}

//endregion

//region Artist extensions

val Artist.all: Observable<List<Artist>>
    get() = App.comp.getCollectionManager().getArtists()

val Artist.albums: Observable<List<Album>>
    get() = App.comp.getCollectionManager().getAlbums(this)

val Artist.songs: Observable<List<Song>>
    get() = App.comp.getCollectionManager().getSongs(this)

val Artist.color: Int
    get() = ColorGenerator.DEFAULT.getColor(name)

//endregion

//region Album extensions

val Album.all: Observable<List<Album>>
    get() = App.comp.getCollectionManager().getAlbums()

val Album.artist: Observable<Artist>
    get() = App.comp.getCollectionManager().getArtist(this)

val Album.songs: Observable<List<Song>>
    get() = App.comp.getCollectionManager().getSongs(this)

//endregion