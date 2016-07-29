package com.github.s0nerik.music.adapters.base

import android.support.v7.widget.RecyclerView

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

open class MediaStoreItem<VH : RecyclerView.ViewHolder>(
        val id: Long = 0
) : AbstractFlexibleItem<VH>() {
    override fun equals(other: Any?): Boolean {
        if (other is MediaStoreItem<*>) {
            return id == other.id
        }

        return false
    }

    override fun hashCode(): Int{
        return id.hashCode()
    }
}
