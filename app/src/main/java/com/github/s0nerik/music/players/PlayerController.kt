package com.github.s0nerik.music.players

import com.github.s0nerik.music.ext.addTo
import rx.subscriptions.CompositeSubscription

class PlayerController(
        val player: BasePlayer,
        private val disposable: CompositeSubscription = CompositeSubscription()
) {
    fun play() { player.setPaused(false).subscribe().addTo(disposable) }
    fun pause() { player.setPaused(true).subscribe().addTo(disposable) }
    fun togglePause() { player.togglePause().subscribe().addTo(disposable) }
    fun playNext() { player.playNextSong().subscribe().addTo(disposable) }
    fun playPrev() { player.playPrevSong().subscribe().addTo(disposable) }
    fun seekTo(ms: Long) { player.seekTo(ms).subscribe().addTo(disposable) }
    fun shuffle(exceptPlayed: Boolean = true) { player.shuffle(exceptPlayed).subscribe().addTo(disposable) }
    fun toggleRepeat() { player.toggleRepeat().subscribe().addTo(disposable) }
    fun dispose() { disposable.clear() }
}