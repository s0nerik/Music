package com.github.s0nerik.music.players

import android.content.Context
import android.media.AudioManager.*
import android.net.Uri
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.events.EPlaybackStateChanged
import com.github.s0nerik.music.ext.sourceUri
import com.github.s0nerik.music.helpers.DelayMeasurer
import com.github.s0nerik.rxbus.RxBus
import com.github.sonerik.rxexoplayer.BasicRxExoPlayer
import com.google.android.exoplayer.ExoPlayer
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer
import com.google.android.exoplayer.MediaCodecSelector
import com.google.android.exoplayer.TrackRenderer
import com.google.android.exoplayer.extractor.ExtractorSampleSource
import com.google.android.exoplayer.upstream.DefaultAllocator
import com.google.android.exoplayer.upstream.DefaultUriDataSource
import org.jetbrains.anko.audioManager
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

abstract class BasePlayer(
        val context: Context
) : BasicRxExoPlayer(context) {
    protected val currentSongSubject: BehaviorSubject<Song> = BehaviorSubject.create<Song>()

    abstract var currentSong: Song?
        get
        protected set

    protected var prepareTimeMeasurer = DelayMeasurer<Long>(10)
    protected var lastState = ExoPlayer.STATE_IDLE
    private val afListener = OnAudioFocusChangeListener {
        when (it) {
            AUDIOFOCUS_LOSS, AUDIOFOCUS_LOSS_TRANSIENT, AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> innerPlayer.sendMessage(currentRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, 0f)
            AUDIOFOCUS_GAIN -> innerPlayer.sendMessage(currentRenderer, MediaCodecAudioTrackRenderer.MSG_SET_VOLUME, 1f)
        }
    }

    private var progressNotifierSubscription: Subscription? = null

    init {
        playerSubject.subscribe {
            when (it) {
                PlayerEvent.STARTED -> {
                    gainAudioFocus()
                    RxBus.post(EPlaybackStateChanged(EPlaybackStateChanged.Type.STARTED, currentSong!!, innerPlayer.currentPosition))
                    progressNotifierSubscription = playbackProgress().subscribe {
                        RxBus.post(EPlaybackStateChanged(EPlaybackStateChanged.Type.PROGRESS, currentSong!!, innerPlayer.currentPosition, innerPlayer.duration))
                    }
                }
                PlayerEvent.PAUSED -> {
                    progressNotifierSubscription?.unsubscribe()
                    RxBus.post(EPlaybackStateChanged(EPlaybackStateChanged.Type.PAUSED, currentSong!!, innerPlayer.currentPosition))
                    abandonAudioFocus()
                }
                PlayerEvent.ENDED, PlayerEvent.IDLE -> {
                    progressNotifierSubscription?.unsubscribe()
                    abandonAudioFocus()
                }
                else -> { }
            }
        }

        playerSubject.filter { it == PlayerEvent.PREPARING }
                .take(1)
                .subscribe { startService(context) }

//        errorSubject.subscribe {
//            Debug.e(it)
//            abandonAudioFocus()
//        }

    }

    abstract fun startService(context: Context)
    abstract fun playNextSong(): Observable<*>
    abstract fun playPrevSong(): Observable<*>
    abstract fun shuffle(exceptPlayed: Boolean = true): Observable<*>
    abstract fun getShuffle(): Boolean
    abstract fun setRepeat(repeat: Boolean): Observable<*>
    abstract fun getRepeat(): Boolean

    fun toggleRepeat(): Observable<*> = setRepeat(!getRepeat())

    fun songChanges() : Observable<Song> {
        return currentSongSubject.skip(1).distinctUntilChanged()
    }

    fun gainAudioFocus() {
        context.audioManager.requestAudioFocus(afListener, STREAM_MUSIC, AUDIOFOCUS_GAIN)
    }

    fun abandonAudioFocus() {
        context.audioManager.abandonAudioFocus(afListener)
    }

    fun playbackProgress() =
            Observable.interval(NOTIFY_INTERVAL, TimeUnit.MILLISECONDS)
                    .onBackpressureLatest()
                    .observeOn(AndroidSchedulers.mainThread())
                    .map { currentPosition }

    fun prepare(song: Song): Observable<PlayerEvent> =
        Observable.defer {
            prepare(song.sourceUri).doOnSubscribe { currentSong = song }
        }

    override fun getRenderer(uri: Uri): TrackRenderer {
        val allocator = DefaultAllocator(bufferSegmentSize)
        return MediaCodecAudioTrackRenderer(
                ExtractorSampleSource(uri,
                        DefaultUriDataSource(context, "LWM"),
                        allocator,
                        bufferSegmentSize * bufferSegmentCount
                ), MediaCodecSelector.DEFAULT
        )
    }

    protected val bufferSegmentSize: Int
        get() = 64 * 1024

    protected val bufferSegmentCount: Int
        get() = 256

    companion object {
        val NOTIFY_INTERVAL = 256L
    }
}