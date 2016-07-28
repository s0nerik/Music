package com.github.s0nerik.music.data.models

import java.io.Serializable

data class Artist(
        val id: Long = 0,
        val numberOfAlbums: Int = 0,
        val numberOfSongs: Int = 0,
        val name: String? = null
) : Serializable {}
