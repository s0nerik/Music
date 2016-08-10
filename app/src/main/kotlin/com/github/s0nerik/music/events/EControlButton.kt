package com.github.s0nerik.music.events

class EControlButton(val type: Type) {
    enum class Type {
        NEXT, PREV, TOGGLE_PAUSE, PAUSE, PLAY
    }
}