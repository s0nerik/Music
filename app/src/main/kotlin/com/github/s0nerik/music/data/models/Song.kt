package com.github.s0nerik.music.data.models

import android.net.Uri
import android.webkit.MimeTypeMap
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
}
