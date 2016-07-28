package com.github.s0nerik.music.data.helpers.db;

enum class Order {
    ASCENDING, DESCENDING
}

open class SortOrder {

    companion object {
        val RANDOM: SortOrder = RandomSortOrder()
    }

    private val order: Map<String, Order>

    constructor(order: Map<String, Order>) {
        this.order = order
    }

    constructor(columns: List<String>, order: Order) {
        this.order = mutableMapOf()
        columns.forEach {
            this.order[it] = order
        }
    }

    override fun toString(): String {
        return order.map {
            when (it.value) {
                Order.ASCENDING -> return "${it.key} ASC"
                Order.DESCENDING -> return "${it.key} DESC"
                else -> return ""
            }
        }.joinToString(", ")
    }
}

class RandomSortOrder : SortOrder(emptyMap()) {
    override fun toString(): String {
        return "random()"
    }
}

class StringSortOrder(private val order: String) : SortOrder(emptyMap()) {
    override fun toString(): String {
        return order
    }
}