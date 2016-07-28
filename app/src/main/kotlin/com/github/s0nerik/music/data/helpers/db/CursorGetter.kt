package com.github.s0nerik.music.data.helpers.db

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.github.s0nerik.music.App
import java.util.*
import javax.inject.Inject

abstract class CursorGetter {

    @Inject
    lateinit var contentResolver: ContentResolver

    init {
        App.inject(this)
    }

    internal abstract val contentUri: Uri
    internal abstract val projection: List<String>
    internal abstract val selection: List<String>
    internal abstract val sortOrder: SortOrder

    fun projectionIndices(): Map<String, Int> {
        val indices = HashMap<String, Int>()

        for (i in 0..projection.size - 1) {
            indices[projection[i]] = i
        }

        return indices
    }

    val cursor: Cursor
        get() {
            return contentResolver.query(
                    contentUri,
                    projection.toTypedArray(),
                    makeSelection(),
                    null,
                    sortOrder.toString()
            )
        }

    private fun makeSelection(): String {
        return selection.joinToString(" AND ")
    }
}
