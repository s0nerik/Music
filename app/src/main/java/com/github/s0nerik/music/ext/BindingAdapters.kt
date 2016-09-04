package com.github.s0nerik.music.ext

import android.databinding.BindingAdapter
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.s0nerik.music.R

@BindingAdapter("bind:coverSrc")
fun setCoverSrc(iv: ImageView, path: String) {
    Glide.with(iv.context)
            .load(path)
            .centerCrop()
            .error(R.drawable.no_cover)
            .placeholder(R.color.md_black_1000)
            .crossFade()
            .into(iv)
}

@BindingAdapter("bind:coverSrc")
fun setCoverSrc(iv: ImageView, uri: Uri) {
    Glide.with(iv.context)
            .load(uri)
            .centerCrop()
            .error(R.drawable.no_cover)
            .placeholder(R.color.md_black_1000)
            .crossFade()
            .into(iv)
}

@BindingAdapter("bind:squareSize")
fun setSquareSize(view: View, size: Float) {
    val layoutParams = view.layoutParams
    layoutParams.width = size.toInt()
    layoutParams.height = size.toInt()
    view.layoutParams = layoutParams
}