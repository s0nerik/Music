package com.github.s0nerik.music.data.models

import android.database.Cursor
import android.provider.BaseColumns._ID
import android.provider.MediaStore.Audio.ArtistColumns.*
import com.github.s0nerik.music.data.helpers.db.CursorFactory
import java.io.Serializable

data class Artist(
        val id: Long = 0,
        val numberOfAlbums: Int = 0,
        val numberOfSongs: Int = 0,
        val name: String? = null
) : Serializable {
    class Factory : CursorFactory<Artist> {
        override fun produce(cursor: Cursor, indices: Map<String, Int>): Artist {
            return Artist(
                    id = cursor.getLong(indices[_ID]!!),
                    numberOfAlbums = cursor.getInt(indices[NUMBER_OF_ALBUMS]!!),
                    numberOfSongs = cursor.getInt(indices[NUMBER_OF_TRACKS]!!),
                    name = cursor.getString(indices[ARTIST]!!)
            )
        }
    }
}
