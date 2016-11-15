package com.github.s0nerik.music.binding_adapters

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.databinding.BindingAdapter
import android.databinding.adapters.ListenerUtil
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v4.view.ViewPager
import android.support.v7.graphics.Palette
import android.view.View
import co.metalab.asyncawait.async
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.github.s0nerik.music.R

@BindingAdapter("squareSize")
fun setSquareSize(view: View, size: Float) {
    val layoutParams = view.layoutParams
    layoutParams.width = size.toInt()
    layoutParams.height = size.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("bgColorFrom")
fun setBackgroundWithCircularTransition(view: View, uri: Uri?) {
    Glide.with(view.context)
            .load(uri)
            .asBitmap()
            .into(object : SimpleTarget<Bitmap>(256, 256) {
                override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                    async {
                        val palette = await { Palette.from(resource).generate() }
                        val swatch = palette.vibrantSwatch ?: palette.dominantSwatch!!

                        with(view) {
                            (background as? ColorDrawable)?.color
                            val startColor = (background as? ColorDrawable)?.color ?: Color.BLACK

                            ObjectAnimator.ofObject(this, "backgroundColor", ArgbEvaluator(), startColor, swatch.rgb)
                                    .setDuration(500)
                                    .start()
                        }
                    }
                }
            })
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