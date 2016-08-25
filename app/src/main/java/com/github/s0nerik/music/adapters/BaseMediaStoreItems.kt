package com.github.s0nerik.music.adapters

import android.support.v7.widget.RecyclerView
import eu.davidea.flexibleadapter.items.AbstractExpandableItem
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.ExpandableViewHolder

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

open class MediaStoreExpandableItem<VH : ExpandableViewHolder, S : IFlexible<*>>(
        val id: Long = 0
) : AbstractExpandableItem<VH, S>() {
    override fun equals(other: Any?): Boolean {
        if (other is MediaStoreExpandableItem<*, *>) {
            return id == other.id
        }

        return false
    }

    override fun hashCode(): Int{
        return id.hashCode()
    }
}