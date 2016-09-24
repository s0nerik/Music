package com.github.s0nerik.music.screens.playback

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.v4.view.ViewPager
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
import org.jetbrains.anko.dip
import org.jetbrains.anko.onClick
import rx.Observable
import java.util.concurrent.TimeUnit

class PlaybackActivity : BaseBoundActivity<ActivityPlaybackBinding>() {
    override val layoutId = R.layout.activity_playback

    lateinit var player: LocalPlayer

    lateinit var bgDrawable: TransitionDrawable
    lateinit var coverDrawable: TransitionDrawable

    private val songItems = mutableListOf<PlaybackSongItem>()
    private val songsAdapter = PlaybackSongsListAdapter(this, songItems)

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

        songItems.clear()
        songItems.addAll(player.queue.queue.map(::PlaybackSongItem))
        songsAdapter.notifyDataSetChanged()

        songsViewPager.adapter = songsAdapter
        songsViewPager.pageMargin = dip(0)
        songsViewPager.offscreenPageLimit = 2
        songsViewPager.setPageTransformer(false, PlaybackSongsPageTransformer())
        songsViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                setSongInfo(songItems[position].song)
            }
        })

        blurView.setBlurredView(background)

        btnNext.onClick { player.playNextSong().subscribe() }
        btnPrev.onClick { player.playPrevSong().subscribe() }
        btnPlayPause.onClick { player.togglePause().subscribe() }
        btnShuffle.onClick { player.queue.shuffle(true) }
        btnRepeat.onClick { player.repeat = !player.repeat }
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
