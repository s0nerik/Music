package com.github.s0nerik.music.services

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import com.github.s0nerik.music.App
import com.github.s0nerik.music.commands.*
import com.github.s0nerik.music.events.EControlButton
import com.github.s0nerik.music.events.EPlaybackChanged
import com.github.s0nerik.music.players.LocalPlayer
import com.github.s0nerik.music.ui.notifications.NowPlayingNotification
import com.github.s0nerik.rxbus.RxBus
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class LocalPlayerService : Service() {
    private val sub = CompositeSubscription()

    @Inject
    lateinit var player: LocalPlayer
    @Inject
    lateinit var audioManager: AudioManager
//    @Inject
//    lateinit var server: WebSocketMessageServer

    var mediaButtonsReceiver: ComponentName? = null

    override fun onCreate() {
        App.comp.inject(this)
        initEventHandlers()
//        player.server = server
//        mediaButtonsReceiver = ComponentName(packageName, MediaButtonIntentReceiver.canonicalName)
        audioManager.registerMediaButtonEventReceiver(mediaButtonsReceiver)
    }

    override fun onDestroy() {
        stopForeground(true)
//        player.stop()
        sub.clear()
        audioManager.unregisterMediaButtonEventReceiver(mediaButtonsReceiver)
    }

    private fun initEventHandlers() {
        RxBus.on(CChangePauseState::class.java).subscribe { onEvent(it) }
        RxBus.on(CSeekTo::class.java).subscribe { onEvent(it) }
        RxBus.on(CSetQueueAndPlay::class.java).subscribe { onEvent(it) }
        RxBus.on(CPlaySongAtPosition::class.java).subscribe { onEvent(it) }
        RxBus.on(CEnqueue::class.java).subscribe { onEvent(it) }
        RxBus.on(EControlButton::class.java).subscribe { onEvent(it) }
        RxBus.on(EPlaybackChanged::class.java).subscribe { onEvent(it) }

//        RxBus.post new CurrentSongAvailableEvent(player.currentSong)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //        makeForeground player.playing
        return Service.START_NOT_STICKY
    }

    private fun makeForeground(isPlaying: Boolean) {
        startForeground(1337, NowPlayingNotification(player.currentSong!!).create(isPlaying))
    }

    // region Event handlers
    private fun onEvent(cmd: CChangePauseState) {
//        player.setPaused(cmd.pause).subscribe()
        //        Observable observable
        //
        //        if (server.started)
        //            if (cmd.pause) {
        //                observable = Observable.merge(player.setPaused(cmd.pause), server.pauseClients())
        //            } else {
        //                observable = Observable.defer {
        //                    server.prepareClients(new PrepareInfo(player.currentSong, System.currentTimeMillis(), player.currentPosition, false, false))
        //                          .map { new ImmutablePair<Collection<WebSocket>, Long>(it, server.recommendedStartTime) }
        //                          .doOnNext { bus.post new CStartPlaybackDelayed(it.right) }
        //                          .concatMap { server.startClients(it.left, it.right) }
        //                }
        //            }
        //        else
        //            observable = player.setPaused(cmd.pause)
        //
        //        observable.subscribe { Debug.d "LocalPlayer setPaused: $it" }
    }

    private fun onEvent(cmd: CSeekTo) {
//        player.seekTo(cmd.position).subscribe {
//            Debug.d("Player sought to: $it")
//        }
    }

    private fun onEvent(cmd: CSetQueueAndPlay) {
        player.queue.clear()
        player.queue.addAll(cmd.queue)
        if (cmd.shuffle) player.queue.shuffle()

//        Observable.concat(player.prepare(cmd.position), player.start())
//                .subscribe { Debug.d("LocalPlayer prepared and started playback") }
    }

    private fun onEvent(cmd: CPlaySongAtPosition) {
//        val prepare = when (cmd.positionType) {
//            CPlaySongAtPosition.PositionType.NEXT -> player.prepareNextSong()
//            CPlaySongAtPosition.PositionType.PREVIOUS -> player.preparePrevSong()
//            CPlaySongAtPosition.PositionType.EXACT -> player.prepare(cmd.position)
//        }
//
//        prepare.concatMap { player.start() }
//                .subscribe()
    }

    private fun onEvent(cmd: CEnqueue) {
        player.queue.addAll(cmd.playlist)
    }

    private fun onEvent(event: EControlButton) {
//        Debug.d(event.toString())
//        when(event.type) {
//            EControlButton.Type.NEXT -> RxBus.post(CPlaySongAtPosition(CPlaySongAtPosition.PositionType.NEXT))
//            EControlButton.Type.PREV -> RxBus.post(CPlaySongAtPosition(CPlaySongAtPosition.PositionType.PREVIOUS))
//            EControlButton.Type.PLAY, EControlButton.Type.PAUSE, EControlButton.Type.TOGGLE_PAUSE -> player.togglePause().subscribe { Debug.d("LocalPlayer toggled pause") }
//        }
    }

    private fun onEvent(e: EPlaybackChanged) {
        if (e.type == EPlaybackChanged.Type.STARTED)
            makeForeground(true)
        else if (e.type == EPlaybackChanged.Type.PAUSED)
            makeForeground(false)
    }

    // endregion
}
