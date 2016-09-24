package com.github.s0nerik.music.binding_adapters

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("bind:squareSize")
fun setSquareSize(view: View, size: Float) {
    val layoutParams = view.layoutParams
    layoutParams.width = size.toInt()
    layoutParams.height = size.toInt()
    view.layoutParams = layoutParams
}