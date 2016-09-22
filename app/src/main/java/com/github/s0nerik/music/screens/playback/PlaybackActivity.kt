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
import com.github.s0nerik.rxbus.RxBus
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_playback.*
import kotlinx.android.synthetic.main.part_playback_control_buttons.*
import org.jetbrains.anko.*
import ru.noties.debug.Debug
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

        songsViewPager.adapter = songsAdapter
        songsViewPager.pageMargin = dip(-48)
        songsViewPager.offscreenPageLimit = 3

        songsViewPager.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
            private val SCALE_INACTIVE = 0.8f
            private val SCALE_ACTIVE = 1f
            private val TRANSLATION_Z_ACTIVE = 4f
            private val TRANSLATION_Z_INACTIVE = 2f
            private val ANIM_DURATION = 200L

            private var lastScrollState = ViewPager.SCROLL_STATE_IDLE

            private var lastSelectedRealPos = -1

            override fun onPageScrollStateChanged(state: Int) {
                if (lastScrollState != ViewPager.SCROLL_STATE_DRAGGING && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    for (i in 0..songsViewPager.childCount-1) {
                        with(songsViewPager.getChildAt(i)) {
                             animate()
                                    .scaleX(SCALE_INACTIVE)
                                    .scaleY(SCALE_INACTIVE)
                                    .translationX(0f)
                                    .translationZ(TRANSLATION_Z_INACTIVE)
                                    .setDuration(ANIM_DURATION)
                                    .start()
                        }
                    }
                }
                lastScrollState = state
            }

            override fun onPageSelected(position: Int) {
                val currentChild = songsViewPager.findViewWithTag(position)
                var realPos: Int = -1
                val childCount = songsViewPager.childCount
                for (i in 0..childCount-1) {
                    with(songsViewPager.getChildAt(i)) {
                        scaleX = SCALE_INACTIVE
                        scaleY = SCALE_INACTIVE
                        translationZ = TRANSLATION_Z_INACTIVE

                        if (currentChild == this) realPos = i
                    }
                }
                lastSelectedRealPos = realPos

                songsViewPager.getChildAt(realPos)
                        ?.animate()
                        ?.scaleX(SCALE_ACTIVE)
                        ?.scaleY(SCALE_ACTIVE)
                        ?.translationZ(TRANSLATION_Z_ACTIVE)
                        ?.setDuration(ANIM_DURATION)
                        ?.start()

                songsViewPager.getChildAt(realPos - 1)
                        ?.animate()
                        ?.translationX(240f)
                        ?.setDuration(ANIM_DURATION)
                        ?.start()

                songsViewPager.getChildAt(realPos + 1)
                        ?.animate()
                        ?.translationX(-240f)
                        ?.setDuration(ANIM_DURATION)
                        ?.start()
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
        binding.song = song
        Observable.interval(16, TimeUnit.MILLISECONDS)
                .take(3, TimeUnit.SECONDS)
                .observeOnMainThread()
                .subscribe { blurView.invalidate() }

        songItems.clear()
        songItems.addAll(player.queue.queue.map { PlaybackSongItem(it) })
        songsAdapter.notifyDataSetChanged()

//        val helper = LinearSnapHelper()
//        helper.attachToRecyclerView(recyclerViewPager)

//        recyclerViewPager.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//            }
//        })

//        val scroller = LinearSmoothScroller(this)
//        recyclerViewPager.layoutManager.startSmoothScroll(scroller)

//        viewPager.offscreenPageLimit = 2
//        viewPager.adapter = object: ViewPagerAdapter() {
//            override fun getCount() = player.queue.size
//
//            override fun getView(position: Int, pager: ViewPager): View {
//                val binding = DataBindingUtil.inflate<ItemPlaybackSongBinding>(
//                        LayoutInflater.from(this@PlaybackActivity), R.layout.item_playback_song, pager, false)
//                binding.song = player.queue.elementAt(position)
//                return binding.root
//            }
//        }
//
//        val coverFlow = CoverFlow.Builder()
//                .with(viewPager)
//                .pagerMargin(0f)
//                .scale(0.3f)
//                .spaceSize(0f)
//                .rotationY(0f)
//                .build()

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
