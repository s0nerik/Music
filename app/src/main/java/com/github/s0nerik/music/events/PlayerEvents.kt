package com.github.s0nerik.music.events

import com.github.s0nerik.music.data.models.Song

class EPlaybackStateChanged(
        val type: Type,
        val song: Song,
        val time: Long,
        val duration: Long = song.duration.toLong()
) {
    enum class Type {
        STARTED, PAUSED, STOPPED, PROGRESS
    }

    val progressPercent: Float
        get() = time * 100f / duration
}

class EPlayerStateChanged(
        val shuffle: Boolean,
        val repeat: Boolean
)