package com.github.s0nerik.music.data.helpers.db.cursor_getters

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import com.github.s0nerik.music.App
import com.github.s0nerik.music.data.helpers.db.SortOrder
import javax.inject.Inject

abstract class CursorGetter {

    @Inject
    lateinit var contentResolver: ContentResolver

    init {
        App.comp.inject(this)
    }

    protected abstract val contentUri: Uri
    protected abstract val projection: List<String>
    protected open val selection: MutableList<String> = mutableListOf()
    protected abstract val sortOrder: SortOrder

    fun projectionIndices(): Map<String, Int> {
        val indices = mutableMapOf<String, Int>()

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
