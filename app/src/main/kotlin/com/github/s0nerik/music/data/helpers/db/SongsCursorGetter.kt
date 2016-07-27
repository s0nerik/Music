//package com.github.s0nerik.music.data.helpers.db
//
//import android.content.ContentResolver
//import android.net.Uri
//import android.provider.MediaStore.Audio.Media
//
//import app.App
//import app.models.Album
//import app.models.Song
//import groovy.transform.CompileStatic
//import javax.inject.Inject
//
//class SongsCursorGetter() : CursorGetter() {
//
//    var contentUri = Media.EXTERNAL_CONTENT_URI
//    val selection =
//    var selection: List<String>? = null
//    var String: `as`? = null
//    var String: `as`? = null
//
//    var projection: List<String>? = null
//
//    var sortOrder = SortOrder()[Media!!.ARTIST]
//    var Media: SortOrder? = null
//
//    private val album: Album
//
////    init {
////        App.get().inject(this)
////    }
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
//
//    companion object {
//
//        private val supportedMimeTypesString = "('" + Song.SUPPORTED_MIME_TYPES.join("','") + "')"
//    }
//    //endregion
//}
