package com.github.s0nerik.music.events

import com.github.s0nerik.music.data.models.Song

class EQueueChanged(
        val type: Type,
        val songs: List<Song> = emptyList()
) {
    enum class Type { SONGS_ADDED, SONGS_REMOVED, SHUFFLED, CLEARED }
}