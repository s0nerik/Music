package com.github.s0nerik.music.players

import android.databinding.ObservableField
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.events.EQueueChanged
import com.github.s0nerik.music.ext.shuffle
import com.github.s0nerik.music.helpers.Stack
import ru.noties.debug.Debug
import rx.Observable
import rx.subjects.PublishSubject
import kotlin.properties.Delegates

class PlaybackQueue(
        val queue: MutableList<Song> = mutableListOf<Song>()
) : Collection<Song> {
    private val eventsSubject = PublishSubject.create<EQueueChanged>().toSerialized()
    val events: Observable<EQueueChanged> = eventsSubject

    override fun contains(element: Song) = queue.contains(element)
    override fun containsAll(elements: Collection<Song>) = queue.containsAll(elements)
    override fun isEmpty() = queue.isEmpty()
    override fun iterator() = queue.iterator()
    override val size: Int
        get() = queue.size

    // Needed only for proper shuffling
    private val played = Stack<Song>()

    val observableCurrentIndex = ObservableField(-1)
    var currentIndex: Int by Delegates.observable(-1, { kProperty, old, new ->
        observableCurrentIndex.set(new)
    })

    val currentSong: Song?
        get() = queue.getOrNull(currentIndex)

    var shuffled = false
        get
        private set

    fun add(vararg songs: Song) {
        queue += songs
        eventsSubject.onNext(EQueueChanged(EQueueChanged.Type.SONGS_ADDED, songs.asList()))
    }

    fun add(pos: Int, vararg songs: Song) {
        queue.addAll(pos, songs.asList())
        eventsSubject.onNext(EQueueChanged(EQueueChanged.Type.SONGS_ADDED, songs.asList()))
    }

    fun addAll(songs: Collection<Song>) {
        queue += songs
        eventsSubject.onNext(EQueueChanged(EQueueChanged.Type.SONGS_ADDED, songs.toList()))
    }

    fun remove(vararg songs: Song) {
        queue -= songs
        eventsSubject.onNext(EQueueChanged(EQueueChanged.Type.SONGS_REMOVED, songs.toList()))
    }

    fun clear() {
        currentIndex = -1
        queue.clear()
        eventsSubject.onNext(EQueueChanged(EQueueChanged.Type.CLEARED))
    }

    fun shuffle(exceptPlayed: Boolean = false) {
        if (exceptPlayed) {
            /**
             * Removes played list from queue list, shuffles queue list and
             * then adds played list to the beginning of queue list.
             */
            queue.removeAll(played)
            queue.shuffle()
            queue.addAll(0, played)

            currentIndex = played.size - 1
        } else {
            played.clear()
            queue.shuffle()
            currentIndex = 0
        }

        shuffled = true
        eventsSubject.onNext(EQueueChanged(EQueueChanged.Type.SHUFFLED, queue))
    }

    /**
     *
     * @return true if successfully moved to the next song, else returns false
     */
    fun moveToNext(circular: Boolean = false): Boolean {
        if (currentIndex + 1 < queue.size) {
            played.push(currentSong!!)
            currentIndex++
            return true
        } else {
            if (circular) {
                currentIndex %= queue.size
                return true
            } else {
                return false
            }
        }
    }

    fun moveToNextAsObservable(circular: Boolean = false): Observable<Song> {
        return Observable.defer {
            if (moveToNext(circular))
                Observable.just(currentSong!!)
            else
                Observable.error(IllegalStateException("Can't move to the next song"))
        }
    }

    /**
     *
     * @return true if successfully moved to the next previous song, else returns false
     */
    fun moveToPrev(): Boolean {
        if (currentIndex - 1 >= 0) {
            played.pop()
            currentIndex--
            return true
        } else {
            return false
        }
    }

    fun moveToPrevAsObservable(circular: Boolean = false): Observable<Song> {
        return Observable.defer {
            if (moveToPrev())
                Observable.just(currentSong!!)
            else
                Observable.error(IllegalStateException("Can't move to the previous song"))
        }
    }

    /**
     *
     * @return true if successfully moved to the next previous song, else returns false
     */
    fun moveTo(position: Int): Boolean {
        if (position >= 0 && position < queue.size) {
            currentIndex = position
            played.push(currentSong!!)
            return true
        } else {
            Debug.e("Can't move to position $position. Queue size: ${queue.size}")
            return false
        }
    }

    fun moveToAsObservable(position: Int): Observable<Song> {
        return Observable.defer {
            if (moveTo(position))
                Observable.just(currentSong!!)
            else
                Observable.error(IllegalStateException("Can't move to the specified position"))
        }
    }
}
