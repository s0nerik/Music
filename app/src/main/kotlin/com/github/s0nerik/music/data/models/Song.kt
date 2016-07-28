package com.github.s0nerik.music.data.models

import android.content.ContentUris
import android.net.Uri
import android.webkit.MimeTypeMap
import com.github.s0nerik.music.data.helpers.CollectionManager
import rx.Observable
import java.io.Serializable
import javax.inject.Inject

data class Song(
        val id: Long = 0,
        val artistId: Long = 0,
        val albumId: Long = 0,
        val duration: Int = 0,
        val title: String = "",
        val source: String = "",
        val artistName: String? = null,
        val albumName: String? = null,
        val lyrics: String? = null,
        val mimeType: String? = null
) : Serializable {
    val durationString: String
        get() {
            var seconds = duration / 1000
            val minutes = seconds / 60
            seconds -= minutes * 60
            return "$minutes:${String.format("%02d", seconds)}"
        }

    val albumArtUri: Uri
        get() = ContentUris.withAppendedId(ARTWORK_URI, albumId)

    val sourceUri: Uri
        get() = Uri.parse("file://$source")

    val album: Observable<Album>
        get() = collectionManager.getAlbum(this)

    val artist: Observable<Artist>
        get() = collectionManager.getArtist(this)

    @Inject
    @Transient
    protected lateinit var collectionManager: CollectionManager

    companion object {
        val SUPPORTED_MIME_TYPES = arrayOf(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("m4a"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp4"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("aac")
        )
        val ARTWORK_URI = Uri.parse("content://media/external/audio/albumart")
    }
}
