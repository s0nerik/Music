package com.github.s0nerik.music.ext

import java.util.*

fun <T> MutableList<T>.shuffle() {
    val rand = Random()
    for (i in 0..size - 1) {
        val randomPosition = rand.nextInt(size)
        swap(i, randomPosition)
    }
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}