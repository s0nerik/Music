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
import android.support.v4.view.ViewPager
import android.view.ViewTreeObserver
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.playback_songs.PlaybackSongItem
import com.github.s0nerik.music.adapters.playback_songs.PlaybackSongsListAdapter
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.databinding.ActivityPlaybackBinding
import com.github.s0nerik.music.events.EPlaybackStateChanged
import com.github.s0nerik.music.ext.currentPositionInMinutes
import com.github.s0nerik.music.ext.observeOnMainThread
import com.github.s0nerik.music.players.LocalPlayer
import com.github.s0nerik.music.ui.views.PlaybackSongsPageTransformer
import com.github.s0nerik.rxbus.RxBus
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_playback.*
import kotlinx.android.synthetic.main.part_playback_control_buttons.*
import org.jetbrains.anko.onClick
import ru.noties.debug.Debug
import rx.Observable
import java.util.concurrent.TimeUnit

class PlaybackActivity : BaseBoundActivity<ActivityPlaybackBinding>() {
    override val layoutId = R.layout.activity_playback

    lateinit var player: LocalPlayer

    private val songItems = mutableListOf<PlaybackSongItem>()
    private val songsAdapter = PlaybackSongsListAdapter(this, songItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = App.comp.getLocalPlayer()
        @Suppress("MISSING_DEPENDENCY_CLASS")
        binding.player = player

        updateSongsList()

        songsViewPager.adapter = songsAdapter
        songsViewPager.setPageTransformer(false, PlaybackSongsPageTransformer())
        songsViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (player.queue.currentIndex != position) {
                    player.playSong(position).subscribe()
                }
            }
        })

        blurView.setBlurredView(background)

        btnNext.onClick { player.playNextSong().subscribe() }
        btnPrev.onClick { player.playPrevSong().subscribe() }
        btnPlayPause.onClick { player.togglePause().subscribe() }
        btnShuffle.onClick {
            player.queue.shuffle(true)
            updateSongsList()
        }
        btnRepeat.onClick { player.repeat = !player.repeat }
    }

    private fun updateSongsList() {
        songItems.clear()
        songItems.addAll(player.queue.queue.map(::PlaybackSongItem))
        songsAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        initEventHandlers()
        initView()
    }

    private fun initEventHandlers() {
        RxBus.on(EPlaybackStateChanged::class.java)
                .bindToLifecycle(this)
                .observeOnMainThread()
                .subscribe { onEvent(it) }
    }

    fun setSongInfo(song: Song) {
        @Suppress("MISSING_DEPENDENCY_CLASS")
        binding.song = song

        Observable.interval(16, TimeUnit.MILLISECONDS)
                .take(3, TimeUnit.SECONDS)
                .observeOnMainThread()
                .subscribe { blurView.invalidate() }
    }

    private fun setPlayButton(playing: Boolean) {
        if (playing) {
            btnPlayPauseIcon.setImageResource(R.drawable.ic_pause_black_24dp)
        } else {
            btnPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        }
    }

    private fun setShuffleButton(enabled: Boolean) {
        btnShuffleIcon.setColorFilter(resources.getColor(if (enabled) R.color.colorPrimary else android.R.color.white, theme))
    }

    private fun setRepeatButton(enabled: Boolean) {
        btnRepeatIcon.setColorFilter(resources.getColor(if (enabled) R.color.colorPrimary else android.R.color.white, theme))
    }

    private fun initView() {
        setSongInfo(player.currentSong!!)
        currentTime.text = player.currentPositionInMinutes
        setPlayButton(player.isPlaying)
        setShuffleButton(player.shuffle)
        setRepeatButton(player.repeat)
    }

    private fun onEvent(e: EPlaybackStateChanged) {
        when (e.type) {
            EPlaybackStateChanged.Type.STARTED -> {
                setSongInfo(e.song)
                setPlayButton(true)
            }
            EPlaybackStateChanged.Type.PAUSED -> {
                setPlayButton(false)
            }
            EPlaybackStateChanged.Type.PROGRESS -> {
                seekBar.progress = (e.progressPercent * 10f).toInt()
                currentTime.text = player.currentPositionInMinutes
            }
            EPlaybackStateChanged.Type.STOPPED -> TODO()
        }
    }
}

@BindingAdapter("bind:playbackBgUri")
fun setPlaybackBgUri(iv: ImageView, uri: Uri) {
    Debug.d("setPlaybackBgUri: $uri")
    var previousDrawable: Drawable = ColorDrawable(ContextCompat.getColor(iv.context, R.color.md_black_1000))
    if (iv.drawable is TransitionDrawable) {
        previousDrawable = (iv.drawable as TransitionDrawable).getDrawable(1)
    }
    Glide.with(iv.context)
            .load(uri)
            .asBitmap()
            .placeholder(previousDrawable)
            .dontAnimate()
//            .skipMemoryCache(true)
            .into(object : BitmapImageViewTarget(iv) {
                override fun setResource(resource: Bitmap?) {
                    val newDrawable =
                            if (resource != null)
                                BitmapDrawable(iv.context.resources, resource)
                            else
                                ColorDrawable(ContextCompat.getColor(iv.context, R.color.md_black_1000))

                    val newTransitionDrawable = TransitionDrawable(arrayOf(previousDrawable, newDrawable))
//                    newTransitionDrawable.isCrossFadeEnabled = true
                    iv.setImageDrawable(newTransitionDrawable)
                    iv.viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            iv.viewTreeObserver.removeOnPreDrawListener(this)
                            newTransitionDrawable.startTransition(500)
                            return true
                        }
                    })
                }
            })
}