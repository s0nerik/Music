package com.github.s0nerik.music.ext

import android.databinding.BindingAdapter
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.s0nerik.music.R

@BindingAdapter("bind:coverSrc")
fun setCoverSrc(iv: ImageView, oldPath: String?, newPath: String) {
    if (newPath != oldPath) {
        Glide.with(iv.context)
                .load(newPath)
                .centerCrop()
                .error(R.drawable.no_cover)
//                .skipMemoryCache(true)
                .placeholder(iv.drawable ?: ColorDrawable(R.color.md_black_1000))
                .crossFade(500)
                .into(iv)
    }
}

@BindingAdapter("bind:coverSrc")
fun setCoverSrc(iv: ImageView, oldUri: Uri?, newUri: Uri) {
    if (newUri != oldUri) {
        Glide.with(iv.context)
                .load(newUri)
                .centerCrop()
                .error(R.drawable.no_cover)
//                .skipMemoryCache(true)
                .placeholder(iv.drawable ?: ColorDrawable(R.color.md_black_1000))
                .crossFade(500)
                .into(iv)
    }
}

@BindingAdapter("bind:squareSize")
fun setSquareSize(view: View, size: Float) {
    val layoutParams = view.layoutParams
    layoutParams.width = size.toInt()
    layoutParams.height = size.toInt()
    view.layoutParams = layoutParams
}