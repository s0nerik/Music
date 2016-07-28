package com.github.s0nerik.music.data.helpers.db

import android.net.Uri
import android.provider.MediaStore.Audio.Media
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.data.models.Song

class SongsCursorGetter() : CursorGetter() {
    override val contentUri: Uri
        get() = Media.EXTERNAL_CONTENT_URI
    override val projection: List<String>
        get() = listOf(
                Media._ID,
                Media.TITLE,
                Media.ARTIST,
                Media.ALBUM,
                Media.DURATION,
                Media.DATA,
                Media.DISPLAY_NAME,
                Media.SIZE,
                Media.ALBUM_ID,
                Media.ARTIST_ID,
                Media.TRACK,
                Media.MIME_TYPE
        )
    override val selection: List<String>
        get() = listOf(
            "${Media.IS_MUSIC} != 0",
            "${Media.MIME_TYPE} in $supportedMimeTypesString"
        )
    override val sortOrder: SortOrder
        get() = SortOrder(listOf(Media.ARTIST, Media.ALBUM, Media.TRACK, Media.DISPLAY_NAME), Order.ASCENDING)

    private var album: Album? = null

////
////    //region Constructors
////    SongsCursorGetter() {
////        App.get().inject(this)
////    }
////
////    SongsCursorGetter(@NonNull Album album) {
////        this()
////        this.album = album
////        selection << ("$Media.ALBUM_ID = $album.id" as String)
////    }
////
////    SongsCursorGetter(@NonNull SortOrder sortOrder) {
////        this()
////        this.sortOrder = sortOrder
////    }
////
////    SongsCursorGetter(@NonNull Album album, @NonNull SortOrder sortOrder) {
////        this(album)
////        this.sortOrder = sortOrder
////    }
////    //endregion
//
//    constructor(album: Album) : this() {
//        this.album = album
//        selection!! shl "\$Media.ALBUM_ID = \$album.id"
//        val String: `as`
//    }
//
//    constructor(sortOrder: SortOrder) : this() {
//        this.sortOrder = sortOrder
//    }
//
//    constructor(album: Album, sortOrder: SortOrder) : this(album) {
//        this.sortOrder = sortOrder
//    }

    companion object {
        val supportedMimeTypesString = "('" + Song.SUPPORTED_MIME_TYPES.joinToString("','") + "')"
    }
//    //endregion
}
