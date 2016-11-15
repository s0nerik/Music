package com.github.s0nerik.music.screens.playback

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v4.graphics.ColorUtils
import android.support.v7.graphics.Palette
import android.view.View
import co.metalab.asyncawait.async
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.players.LocalPlayer
import com.github.s0nerik.music.players.PlayerController
import kotlinx.android.synthetic.main.activity_playback.view.*
import kotlinx.android.synthetic.main.part_playback_control_buttons.view.*

class PlaybackViewModel(
        val ctx: Context,
        val player: LocalPlayer,

        val playerController: PlayerController = PlayerController(player),

        val song: ObservableField<Song?> = player.observableCurrentSong,
        val currentSongIndex: ObservableField<Int> = player.queue.observableCurrentIndex,
        val progressPercent: ObservableFloat = player.observableProgressPercent,
        val positionInMinutes: ObservableField<String> = player.observablePositionInMinutes
) {
    fun playAtIndex(position: Int) { playerController.playAtIndex(position) }
}

@BindingAdapter("bottomBarBgColorSourceUri")
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
                            btnShuffleIcon.setColorFilter(swatch.bodyTextColor)
                            btnRepeatIcon.setColorFilter(swatch.bodyTextColor)
                            btnPlayPauseIcon.setColorFilter(swatch.bodyTextColor)
                            btnPrevIcon.setColorFilter(swatch.bodyTextColor)
                            btnNextIcon.setColorFilter(swatch.bodyTextColor)

                            seekBar.progressDrawable.setColorFilter(
                                    ColorUtils.blendARGB(swatch.rgb, ColorUtils.setAlphaComponent(if (isColorDark(swatch.bodyTextColor)) Color.WHITE else Color.BLACK, 128), 0.33f),
                                    PorterDuff.Mode.SRC_ATOP
                            )

                            (background as? ColorDrawable)?.color
                            val startColor = (background as? ColorDrawable)?.color ?: Color.BLACK

                            ObjectAnimator.ofObject(this, "backgroundColor", ArgbEvaluator(), startColor, swatch.rgb)
                                    .setDuration(500)
                                    .start()
                        }
                    }
                }

                fun isColorDark(color: Int): Boolean {
                    val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
                    return darkness < 0.5
                }
            })
}
