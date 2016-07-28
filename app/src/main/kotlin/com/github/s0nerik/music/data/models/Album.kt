package com.github.s0nerik.music.data.models

import com.github.s0nerik.music.App
import com.github.s0nerik.music.data.helpers.CollectionManager
import rx.Observable

import java.io.Serializable

import javax.inject.Inject

data class Album(
        val id: Long = 0,
        val year: Int = 0,
        val songsCount: Int = 0,
        val title: String? = null,
        val artistName: String? = null,
        val albumArtPath: String? = null,
        val artistId: Long = 0
) : Serializable {
    init {
        App.inject(this)
    }

    val artist: Observable<Artist>
        get() = collectionManager.getArtist(this)

    val songs: Observable<List<Song>>
        get() = collectionManager.getSongs(this)

    @Inject
    @Transient
    protected lateinit var collectionManager: CollectionManager
}
