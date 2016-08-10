package com.github.s0nerik.music.commands

import com.github.s0nerik.music.data.models.Song

data class CChangePauseState(
        val pause: Boolean
)

data class CEnqueue (
    val playlist: List<Song>
)

data class CPlaySongAtPosition(
        val positionType: PositionType,
        val position: Int = -1
) {
    enum class PositionType {
        NEXT, PREVIOUS, EXACT
    }
}

data class CRequestPlaySong (
        val song: Song
)

data class CSeekTo(
    val position: Long
)

data class CSetQueueAndPlay(
        val queue: List<Song>,
        val position: Int = 0,
        val shuffle: Boolean = false
)

data class CStartPlaybackDelayed(
    val startAt: Long = System.currentTimeMillis()
)