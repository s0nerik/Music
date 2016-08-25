package com.github.s0nerik.music.adapters

import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.MenuRes
import android.support.v4.view.GravityCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.s0nerik.music.R

class Sorter<T>(
        val context: Context,
        @MenuRes val sortMenuId: Int,
        @IdRes sortActionId: Int,
        val adapter: RecyclerView.Adapter<*>,
        val sortableList: MutableList<T>,
        val sorters: Map<Int, (T) -> Comparable<*>>,
        val sortingBubbleTextProviders: Map<Int, (T) -> String> = emptyMap()
) {
    @IdRes private var sortIconId = R.drawable.sort_ascending
        get() = if (orderAscending) R.drawable.sort_ascending else R.drawable.sort_descending

    @IdRes private var sortActionId = 0
        set(value) {
            field = value
            if (adapter is BubbleTextProvider<*> && sortingBubbleTextProviders.isNotEmpty())
                adapter.bubbleTextProvider = (sortingBubbleTextProviders[field] as (Any?) -> String)!!
        }

    init {
        this.sortActionId = sortActionId
    }

    private var orderAscending = true

    fun sortItems() {
        if (orderAscending)
            sortableList.sortBy(sorters[sortActionId]!! as (T) -> Comparable<Any>)
        else
            sortableList.sortByDescending(sorters[sortActionId]!! as (T) -> Comparable<Any>)

        adapter.notifyDataSetChanged()
    }

    fun showSortPopup(anchor: View) {
        val menu = PopupMenu(context, anchor, GravityCompat.END)
        menu.inflate(sortMenuId)

//        try {
//            Class<?> classPopupMenu = Class.forName(menu.class.name)
//            Field mPopup = classPopupMenu.getDeclaredField("mPopup")
//            mPopup.setAccessible(true)
//            Object menuPopupHelper = mPopup.get(menu)
//            Class<?> classPopupHelper = Class.forName(menuPopupHelper.class.name)
//            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class)
//            setForceIcons.invoke(menuPopupHelper, true)
//        } catch (Exception e) {
//            e.printStackTrace()
//        }
        for (i in 0 until menu.menu.size()) {
            menu.menu.getItem(i).icon = null
        }
        val currentSortItem = menu.menu.findItem(sortActionId)

        if (orderAscending) {
            currentSortItem?.icon = context.resources.getDrawable(sortIconId, null)
        } else {
            currentSortItem?.icon = context.resources.getDrawable(sortIconId, null)
        }

        menu.setOnMenuItemClickListener { onSortActionIdSelected(it.itemId) }
        menu.show()
    }

    private fun onSortActionIdSelected(itemId: Int): Boolean {
        if (sortActionId == itemId)
            orderAscending = !orderAscending

        sortActionId = itemId
        sortItems()

        return false
    }
}
