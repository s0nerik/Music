package com.github.s0nerik.music.players

import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import android.databinding.ObservableLong
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.ext.currentPositionInMinutes
import com.github.s0nerik.music.ext.durationInMinutes
import rx.Subscription

abstract class BaseBoundPlayer(context: Context) : BasePlayer(context) {
    final override var currentSong: Song?
        get() = currentSongSubject.value
        protected set(value) {
            currentSongSubject.onNext(value)
            observableCurrentSong.set(value)
        }

    val observablePosition: ObservableLong = ObservableLong(currentPosition)
    val observablePositionInMinutes: ObservableField<String> = ObservableField(currentPositionInMinutes)

    val observableDuration: ObservableLong = ObservableLong(duration)
    val observableDurationInMinutes: ObservableField<String> = ObservableField(durationInMinutes)

    val observablePlayingState: ObservableBoolean = ObservableBoolean(isPlaying)

    val observableProgressPercent: ObservableFloat = ObservableFloat(observablePosition.get() * 100f / observableDuration.get())

    val observableCurrentSong: ObservableField<Song?> = ObservableField(currentSong)

    val observableShuffleState: ObservableBoolean = ObservableBoolean(false)
    val observableRepeatState: ObservableBoolean = ObservableBoolean(false)

    private var progressNotifierSubscription: Subscription? = null

    init {
        playerSubject.subscribe {
            when (it) {
                PlayerEvent.STARTED -> {
                    gainAudioFocus()
                    observablePlayingState.set(true)
                    observableDuration.set(duration)
                    observableDurationInMinutes.set(durationInMinutes)
                    progressNotifierSubscription = playbackProgress().subscribe {
                        observablePosition.set(it)
                        observablePositionInMinutes.set(currentPositionInMinutes)
                        observableProgressPercent.set(it * 100f / observableDuration.get())
                    }
                }
                PlayerEvent.PAUSED -> {
                    observablePlayingState.set(false)
                    progressNotifierSubscription?.unsubscribe()
                }
                PlayerEvent.ENDED, PlayerEvent.IDLE -> {
                    observablePlayingState.set(false)
                    progressNotifierSubscription?.unsubscribe()
                }
                else -> { }
            }
        }
    }
}