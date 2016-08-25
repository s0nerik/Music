package com.github.s0nerik.music.ui.behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip

class ScrollAwareFABBehavior1(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs) {
    private val toolbarHeight: Int

    init {
        toolbarHeight = context.dip(56)
    }

    override fun layoutDependsOn(parent: CoordinatorLayout?, fab: FloatingActionButton?, dependency: View?): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout?, fab: FloatingActionButton?, dependency: View?): Boolean {
        if (dependency is AppBarLayout) {
            val lp = fab!!.layoutParams as CoordinatorLayout.LayoutParams
            val fabBottomMargin = lp.bottomMargin
            val distanceToScroll = fab.height + fabBottomMargin
            val ratio = dependency.y / toolbarHeight
            fab.translationY = -distanceToScroll * ratio
        }

        return true
    }
}
