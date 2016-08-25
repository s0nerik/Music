package com.github.s0nerik.music.data.helpers.db.cursor_getters

import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS
import android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
import android.provider.MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
import android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Audio.AudioColumns.ARTIST
import com.github.s0nerik.music.data.helpers.db.SortOrder
import com.github.s0nerik.music.data.helpers.db.StringSortOrder

class ArtistsCursorGetter() : CursorGetter() {
    override val contentUri: Uri
        get() = EXTERNAL_CONTENT_URI

    override val projection: List<String>
        get() = listOf(
                _ID,
                ARTIST,
                NUMBER_OF_ALBUMS,
                NUMBER_OF_TRACKS
        )

    override val sortOrder: SortOrder = StringSortOrder(DEFAULT_SORT_ORDER)

    constructor(id: Long) : this() {
        selection += "$_ID = $id"
    }
}