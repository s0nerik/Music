package com.github.s0nerik.music.screens.playback

import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.playback_songs.PlaybackSongItem
import com.github.s0nerik.music.adapters.playback_songs.PlaybackSongsListAdapter
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.databinding.ActivityPlaybackBinding
import com.github.s0nerik.music.players.LocalPlayer
import com.github.s0nerik.music.ui.views.PlaybackSongsPageTransformer
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_playback.*
import ru.noties.debug.Debug

class PlaybackActivity : BaseBoundActivity<ActivityPlaybackBinding>() {
    override val layoutId = R.layout.activity_playback

    lateinit var player: LocalPlayer

    private val songItems = mutableListOf<PlaybackSongItem>()
    private val songsAdapter = PlaybackSongsListAdapter(this, songItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = App.comp.getLocalPlayer()
        @Suppress("MISSING_DEPENDENCY_CLASS")
        binding.viewModel = PlaybackViewModel(this, player)

        player.queue.events.bindToLifecycle(this).subscribe { updateSongsList() }

        updateSongsList()

        songsViewPager.adapter = songsAdapter
        songsViewPager.setPageTransformer(false, PlaybackSongsPageTransformer())
    }

    private fun updateSongsList() {
        songItems.clear()
        songItems.addAll(player.queue.queue.map(::PlaybackSongItem))
        songsAdapter.notifyDataSetChanged()
    }
}

@BindingAdapter("playbackBgUri")
fun setPlaybackBgUri(iv: ImageView, uri: Uri?) {
    Debug.d("setPlaybackBgUri: $uri")
    var previousDrawable: Drawable = ColorDrawable(ContextCompat.getColor(iv.context, R.color.md_black_1000))
    (iv.drawable as? TransitionDrawable)?.apply {
        previousDrawable = getDrawable(1)
    }
    Glide.with(iv.context)
            .load(uri)
            .asBitmap()
            .placeholder(previousDrawable)
            .error(R.drawable.no_cover)
            .dontAnimate()
            .into(object : BitmapImageViewTarget(iv) {
                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                    val newDrawable = errorDrawable ?: ColorDrawable(ContextCompat.getColor(iv.context, R.color.md_black_1000))

                    val newTransitionDrawable = TransitionDrawable(arrayOf(previousDrawable, newDrawable))
                    iv.setImageDrawable(newTransitionDrawable)
                    newTransitionDrawable.startTransition(500)
                }

                override fun setResource(resource: Bitmap?) {
                    val newDrawable =
                            if (resource != null)
                                BitmapDrawable(iv.context.resources, resource)
                            else
                                ColorDrawable(ContextCompat.getColor(iv.context, R.color.md_black_1000))

                    val newTransitionDrawable = TransitionDrawable(arrayOf(previousDrawable, newDrawable))
                    iv.setImageDrawable(newTransitionDrawable)
                    newTransitionDrawable.startTransition(500)
                }
            })
}