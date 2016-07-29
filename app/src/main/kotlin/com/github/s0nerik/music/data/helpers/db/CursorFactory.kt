package com.github.s0nerik.music.data.helpers.db

import android.database.Cursor

interface CursorFactory<out T> {
    fun produce(cursor: Cursor, indices: Map<String, Int>): T
}