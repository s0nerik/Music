package com.github.s0nerik.music.data.models

import com.github.s0nerik.music.App
import com.github.s0nerik.music.data.helpers.CollectionManager
import rx.Observable
import java.io.Serializable
import javax.inject.Inject

data class Artist(
        val id: Long = 0,
        val numberOfAlbums: Int = 0,
        val numberOfSongs: Int = 0,
        val name: String? = null
) : Serializable {
    init {
        App.inject(this)
    }

    val albums: Observable<List<Album>>
        get() = collectionManager.getAlbums(this)

    @Inject
    @Transient
    protected lateinit var collectionManager: CollectionManager
}
