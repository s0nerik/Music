package com.github.s0nerik.music.adapters.base

import eu.davidea.flexibleadapter.items.AbstractExpandableItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.ExpandableViewHolder

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
