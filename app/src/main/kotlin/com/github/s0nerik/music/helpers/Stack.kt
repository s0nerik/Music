package com.github.s0nerik.music.helpers;

class Stack<T>(
        val items: MutableList<T> = mutableListOf()
): Collection<T> {
    override fun contains(element: T): Boolean = items.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = items.containsAll(elements)

    override fun iterator(): Iterator<T> = items.iterator()

    override fun isEmpty() = items.isEmpty()

    override val size: Int
        get() = items.size

    fun push(vararg elements: T) {
        items += elements
    }

    fun pop():T? {
        if (isEmpty()) {
            return null
        } else {
            return items.removeAt(size - 1)
        }
    }

    fun peek():T? = items.lastOrNull()

    fun clear() = items.clear()

    override fun toString() = this.items.toString()
}