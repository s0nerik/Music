package com.github.s0nerik.music.helpers

import org.apache.commons.lang3.time.StopWatch


class DelayMeasurer<T : Number>(size: Int) : AveragingCollection<T>(size) {
    private val stopwatch = StopWatch()

    fun start() {
        if (stopwatch.isStarted) return
        stopwatch.start()
    }

    fun stop() {
        if (!stopwatch.isStarted) return
        stopwatch.stop()
        add(stopwatch.time as T)
        stopwatch.reset()
    }

    fun cancel() {
        stopwatch.reset()
    }
}
