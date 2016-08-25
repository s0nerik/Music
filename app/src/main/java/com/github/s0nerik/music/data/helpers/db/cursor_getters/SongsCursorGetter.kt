package com.github.s0nerik.music.data.helpers.db.cursor_getters

import android.net.Uri
import android.provider.MediaStore.Audio.Media.*
import com.github.s0nerik.music.data.helpers.db.Order
import com.github.s0nerik.music.data.helpers.db.SortOrder
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.data.models.Song

class SongsCursorGetter() : CursorGetter() {
    override val contentUri: Uri
        get() = EXTERNAL_CONTENT_URI

    override val projection: List<String>
        get() = listOf(
                _ID,
                TITLE,
                ARTIST,
                ALBUM,
                DURATION,
                DATA,
                DISPLAY_NAME,
                SIZE,
                ALBUM_ID,
                ARTIST_ID,
                TRACK,
                MIME_TYPE
        )

    override val selection: MutableList<String>
        get() = mutableListOf(
            "$IS_MUSIC != 0",
            "$MIME_TYPE in $supportedMimeTypesString"
        )

    override var sortOrder: SortOrder = SortOrder(listOf(
            ARTIST, ALBUM, TRACK, DISPLAY_NAME
    ), Order.ASCENDING)

    private var album: Album? = null

    constructor(album: Album) : this() {
        this.album = album
        selection += "${ALBUM_ID} = ${album.id}"
    }

    constructor(sortOrder: SortOrder) : this() {
        this.sortOrder = sortOrder
    }

    constructor(album: Album, sortOrder: SortOrder) : this(album) {
        this.sortOrder = sortOrder
    }

    companion object {
        val supportedMimeTypesString = "('${Song.SUPPORTED_MIME_TYPES.joinToString("','")}')"
    }
}
