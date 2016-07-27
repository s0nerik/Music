//package com.github.s0nerik.music.data.helpers.db
//
//import android.net.Uri
//
//import app.App
//import groovy.transform.CompileStatic
//
//import android.provider.BaseColumns._ID
//import android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS
//import android.provider.MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS
//import android.provider.MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
//import android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
//import android.provider.MediaStore.Audio.AudioColumns.ARTIST
//
//@CompileStatic
//internal class ArtistsCursorGetter() : CursorGetter() {
//
//    var contentUri = EXTERNAL_CONTENT_URI
//    var projection: List<String>? = null
//    var selection: List<String>? = null
//    var sortOrder: SortOrder = StringSortOrder(DEFAULT_SORT_ORDER)
//
//    init {
//        App.get().inject(this)
//    }
//
//    constructor(id: Long) : this() {
//        selection!! shl "$_ID = \$id"
//        val String: `as`
//    }
//}