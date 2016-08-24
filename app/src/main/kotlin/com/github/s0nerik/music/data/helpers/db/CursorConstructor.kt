package com.github.s0nerik.music.data.helpers.db

import com.github.s0nerik.music.data.helpers.db.cursor_getters.CursorGetter
import ru.noties.debug.Debug
import rx.Observable

class CursorConstructor {
    companion object {
        fun <T> fromCursorGetter(factory: CursorFactory<T>, cursorGetter: CursorGetter, check: (T) -> Boolean = { true }): Observable<T> {
            return Observable.create<T>({ subscriber ->
                val cursor = cursorGetter.cursor

                try {
                    if (cursor.moveToFirst()) {
                        val indices = cursorGetter.projectionIndices()

                        do {
                            val item = factory.produce(cursor, indices)
                            if (check(item)) subscriber.onNext(item)
                        } while (cursor.moveToNext())
                    }
                } catch (t: Throwable) {
                    Debug.e(t)
                } finally {
                    cursor.close()
                    subscriber.onCompleted()
                }
            })
        }
    }
}
