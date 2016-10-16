package com.github.s0nerik.music.binding_adapters

import android.databinding.BindingAdapter
import android.databinding.adapters.ListenerUtil
import android.support.v4.view.ViewPager
import android.view.View
import com.github.s0nerik.music.R

@BindingAdapter("squareSize")
fun setSquareSize(view: View, size: Float) {
    val layoutParams = view.layoutParams
    layoutParams.width = size.toInt()
    layoutParams.height = size.toInt()
    view.layoutParams = layoutParams
}

interface OnPageScrollStateChanged {
    fun onPageScrollStateChanged(state: Int)
}

interface OnPageScrolled {
    fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int)
}

interface OnPageSelected {
    fun onPageSelected(position: Int)
}

@BindingAdapter(value = *arrayOf(
    "onPageScrollStateChanged",
    "onPageScrolled",
    "onPageSelected"
), requireAll = false)
fun setViewPagerListeners(view: ViewPager,
                          scrollStateChanged: OnPageScrollStateChanged?,
                          scrolled: OnPageScrolled?,
                          selected: OnPageSelected?) {
    var newListener: ViewPager.OnPageChangeListener? = null
    if (scrollStateChanged != null || scrolled != null || selected != null) {
        newListener = object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                selected?.onPageSelected(position)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                scrolled?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageScrollStateChanged(state: Int) {
                scrollStateChanged?.onPageScrollStateChanged(state)
            }
        }
    }

    val oldListener = ListenerUtil.trackListener(view,
            newListener, R.id.viewPagerListener)
    if (oldListener != null) {
        view.removeOnPageChangeListener(oldListener)
    }
    if (newListener != null) {
        view.addOnPageChangeListener(newListener)
    }
}