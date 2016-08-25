package com.github.s0nerik.music.data.models

import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore.Audio.AlbumColumns.*
import android.provider.MediaStore.Audio.AudioColumns.ALBUM
import android.provider.MediaStore.Audio.AudioColumns.ARTIST_ID
import com.github.s0nerik.music.data.helpers.db.CursorFactory
import java.io.Serializable

data class Album(
        val id: Long = 0,
        val year: Int = 0,
        val songsCount: Int = 0,
        val title: String = "",
        val artistName: String = "",
        val albumArtPath: String = "",
        val artistId: Long = 0
) : Serializable {
    class Factory : CursorFactory<Album> {
        override fun produce(cursor: Cursor, indices: Map<String, Int>): Album {
            return Album(
                    id = cursor.getLong(indices[BaseColumns._ID]!!),
                    title = cursor.getString(indices[ALBUM]!!) ?: "",
                    artistName = cursor.getString(indices[ARTIST]!!) ?: "",
                    year = cursor.getInt(indices[FIRST_YEAR]!!),
                    albumArtPath = cursor.getString(indices[ALBUM_ART]!!) ?: "",
                    songsCount = cursor.getInt(indices[NUMBER_OF_SONGS]!!),
                    artistId = cursor.getLong(indices[ARTIST_ID]!!)
            )
        }
    }
}
