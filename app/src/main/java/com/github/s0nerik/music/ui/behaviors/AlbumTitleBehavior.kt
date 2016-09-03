package com.github.s0nerik.music.ui.behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip

class AlbumTitleBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {
    private val toolbarHeight: Int

    init {
        toolbarHeight = context.dip(56)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, title: View, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, title: View, dependency: View): Boolean {
        if (dependency is AppBarLayout) {
            val lp = title.layoutParams as CoordinatorLayout.LayoutParams
            val fabBottomMargin = lp.bottomMargin
            val distanceToScroll = title.height + fabBottomMargin
            val ratio = dependency.y / toolbarHeight
            title.translationY = -distanceToScroll * ratio
        }

        return true
    }
}
