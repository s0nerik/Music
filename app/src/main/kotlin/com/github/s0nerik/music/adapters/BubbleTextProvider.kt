package com.github.s0nerik.music.adapters

interface BubbleTextProvider<T> {
    var bubbleTextProvider: (T) -> String
}
