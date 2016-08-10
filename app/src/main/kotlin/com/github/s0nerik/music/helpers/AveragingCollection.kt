package com.github.s0nerik.music.helpers

import org.apache.commons.collections4.queue.CircularFifoQueue

open class AveragingCollection<T : Number> : CircularFifoQueue<T> {
    constructor(): super()
    constructor(size: Int): super(size)

    override fun add(element: T): Boolean {
        val result = super.add(element)
        average = sumBy { it.toInt() / size } as T
        return result
    }

    var average: T = 0 as T
        get
        private set
}
