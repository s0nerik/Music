//package com.github.s0nerik.music.data.helpers.db
//
//import android.net.Uri
//
//import app.App
//import app.models.Artist
//import groovy.transform.CompileStatic
//
//import android.provider.BaseColumns._ID
//import android.provider.MediaStore.Audio.AlbumColumns.ALBUM_ART
//import android.provider.MediaStore.Audio.AlbumColumns.FIRST_YEAR
//import android.provider.MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS
//import android.provider.MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
//import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
//import android.provider.MediaStore.Audio.AudioColumns.ALBUM
//import android.provider.MediaStore.Audio.AudioColumns.ARTIST
//import android.provider.MediaStore.Audio.AudioColumns.ARTIST_ID
//
//class AlbumsCursorGetter() : CursorGetter() {
//
//    var contentUri = EXTERNAL_CONTENT_URI
//
//    var projection: List<String>? = null
//
//    var selection: List<String>? = null
//
//    var sortOrder: SortOrder = StringSortOrder(DEFAULT_SORT_ORDER)
//
//    init {
//        App.get().inject(this)
//    }
//
//    constructor(artist: Artist) : this() {
//        selection!! shl "\$ARTIST_ID = \$artist.id"
//        val String: `as`
//    }
//
//    constructor(id: Long) : this() {
//        selection!! shl "$_ID = \$id"
//        val String: `as`
//    }
//}
