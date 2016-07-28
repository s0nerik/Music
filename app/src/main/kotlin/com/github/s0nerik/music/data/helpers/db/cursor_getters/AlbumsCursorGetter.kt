package com.github.s0nerik.music.data.helpers.db.cursor_getters

import android.provider.BaseColumns._ID
import android.provider.MediaStore.Audio.AlbumColumns.ALBUM_ART
import android.provider.MediaStore.Audio.AlbumColumns.FIRST_YEAR
import android.provider.MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS
import android.provider.MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Audio.AudioColumns.ALBUM
import android.provider.MediaStore.Audio.AudioColumns.ARTIST
import android.provider.MediaStore.Audio.AudioColumns.ARTIST_ID
import com.github.s0nerik.music.data.helpers.db.SortOrder
import com.github.s0nerik.music.data.helpers.db.StringSortOrder

import com.github.s0nerik.music.data.models.Artist

class AlbumsCursorGetter() : CursorGetter() {
    override var contentUri = EXTERNAL_CONTENT_URI

    override val projection: List<String>
        get() = listOf(
                _ID,
                ALBUM,
                ALBUM_ART,
                ARTIST,
                FIRST_YEAR,
                NUMBER_OF_SONGS,
                ARTIST_ID
        )

    override var sortOrder: SortOrder = StringSortOrder(DEFAULT_SORT_ORDER)

    constructor(artist: Artist) : this() {
        selection += "$ARTIST_ID = ${artist.id}"
    }

    constructor(id: Long) : this() {
        selection += "$_ID = $id"
    }
}
