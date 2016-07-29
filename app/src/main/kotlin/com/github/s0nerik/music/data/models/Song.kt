package com.github.s0nerik.music.data.models

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.MediaStore.Audio.AudioColumns.*
import android.provider.MediaStore.MediaColumns.MIME_TYPE
import android.webkit.MimeTypeMap
import com.github.s0nerik.music.data.helpers.db.CursorFactory
import java.io.Serializable

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
    companion object {
        val SUPPORTED_MIME_TYPES: List<String> = arrayOf("mp3", "m4a", "mp4", "aac")
                .map { MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) }

        val ARTWORK_URI = Uri.parse("content://media/external/audio/albumart")!!
    }

    class Factory : CursorFactory<Song> {
        override fun produce(cursor: Cursor, indices: Map<String, Int>): Song {
            return Song(
                    id = cursor.getLong(indices[_ID]!!),
                    artistId = cursor.getLong(indices[ARTIST_ID]!!),
                    albumId = cursor.getLong(indices[ALBUM_ID]!!),
                    title = cursor.getString(indices[TITLE]!!),
                    artistName = cursor.getString(indices[ARTIST]!!),
                    albumName = cursor.getString(indices[ALBUM]!!),
                    source = cursor.getString(indices[DATA]!!),
                    duration = cursor.getInt(indices[DURATION]!!),
                    mimeType = cursor.getString(indices[MIME_TYPE]!!)
            )
        }
    }
}