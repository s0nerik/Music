package com.github.s0nerik.music.data.models

import java.io.Serializable

data class Album(
        val id: Long = 0,
        val year: Int = 0,
        val songsCount: Int = 0,
        val title: String? = null,
        val artistName: String? = null,
        val albumArtPath: String? = null,
        val artistId: Long = 0
) : Serializable {}
