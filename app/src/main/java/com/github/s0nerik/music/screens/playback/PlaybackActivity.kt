package com.github.s0nerik.music.screens.playback

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.databinding.ActivityPlaybackBinding
import com.github.s0nerik.music.events.EPlaybackStateChanged
import com.github.s0nerik.music.ext.currentPositionInMinutes
import com.github.s0nerik.music.ext.observeOnMainThread
import com.github.s0nerik.music.players.BasePlayer
import com.github.s0nerik.rxbus.RxBus
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_playback.*
import kotlinx.android.synthetic.main.part_playback_control_buttons.*
import rx.Observable
import java.util.concurrent.TimeUnit

class PlaybackActivity : BaseBoundActivity<ActivityPlaybackBinding>() {
    override val layoutId = R.layout.activity_playback

    lateinit var player: BasePlayer

    lateinit var bgDrawable : TransitionDrawable
    lateinit var coverDrawable : TransitionDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = App.comp.getLocalPlayer()
        if (savedInstanceState == null) {
            val blackDrawables = arrayOf(ColorDrawable(Color.BLACK), ColorDrawable(Color.BLACK))

            coverDrawable = TransitionDrawable(blackDrawables)
            coverDrawable.isCrossFadeEnabled = true

            bgDrawable = TransitionDrawable(blackDrawables)
            bgDrawable.isCrossFadeEnabled = true
        }

        blurView.setBlurredView(background)
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
        binding.song = song
        Observable.interval(16, TimeUnit.MILLISECONDS)
                .take(3, TimeUnit.SECONDS)
                .observeOnMainThread()
                .subscribe { blurView.invalidate() }
//        getCoverBitmap(song)
//                .concatMap({ Bitmap original -> blurer.blurAsObservable(original).map { new Tuple2(original, it) } } as Func1)
//                .applySchedulers()
//                .subscribe { Tuple2<Bitmap, Bitmap> covers -> changeCover covers.first, covers.second }
    }

//    private fun changeCover(newCover: Bitmap, bg: Bitmap) {
//        coverDrawable = TransitionDrawable(arrayOf(coverDrawable.getDrawable(1), BitmapDrawable(resources, newCover)))
//        cover.setImageDrawable(coverDrawable)
//        coverDrawable.startTransition(1000)
//
//        Observable.timer(500, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    bgDrawable = TransitionDrawable(arrayOf(bgDrawable.getDrawable(1), BitmapDrawable(resources, bg)))
//                    background.setImageDrawable(bgDrawable)
//                    bgDrawable.startTransition(1000)
//                }
//    }

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
        when(e.type) {
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
