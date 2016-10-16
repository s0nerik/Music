package com.github.s0nerik.music.ext

import com.github.s0nerik.music.players.BasePlayer

val BasePlayer.currentPositionInMinutes: String
    get() {
        var seconds = currentPosition / 1000
        val minutes = seconds / 60
        seconds -= minutes * 60
        return "$minutes:${String.format("%02d", seconds)}"
    }

val BasePlayer.durationInMinutes: String
    get() {
        var seconds = duration / 1000
        val minutes = seconds / 60
        seconds -= minutes * 60
        return "$minutes:${String.format("%02d", seconds)}"
    }