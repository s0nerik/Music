package com.github.s0nerik.music.screens.playback

import android.content.Context
import com.github.s0nerik.music.players.LocalPlayer
import com.github.s0nerik.music.players.PlayerController

class PlaybackViewModel(
        val ctx: Context,
        val player: LocalPlayer
) {
    val playerController = PlayerController(player)

    val song = player.observableCurrentSong
    val currentSongIndex = player.queue.observableCurrentIndex
    val progressPercent = player.observableProgressPercent
    val positionInMinutes = player.observablePositionInMinutes

    fun playAtIndex(position: Int) { playerController.playAtIndex(position) }
}