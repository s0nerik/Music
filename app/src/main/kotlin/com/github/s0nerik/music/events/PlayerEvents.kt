package com.github.s0nerik.music.events

import com.github.s0nerik.music.data.models.Song

class EPlaybackChanged(
        val type: Type,
        val song: Song,
        val time: Long
) {
    enum class Type {
        STARTED, PAUSED, STOPPED
    }
}

class ESongChanged(val song: Song)

class EPlaybackProgress(
        val progress: Long,
        val duration: Int
) {
    val progressPercent: Float
        get() = progress * 100f / duration
}

class EPlayerStateChanged(
        val shuffle: Boolean,
        val repeat: Boolean
)