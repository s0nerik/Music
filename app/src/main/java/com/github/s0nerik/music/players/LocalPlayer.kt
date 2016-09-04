package com.github.s0nerik.music.players

import android.content.Context
import android.content.Intent
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.events.EPlayerStateChanged
import com.github.s0nerik.music.services.LocalPlayerService
import com.github.s0nerik.rxbus.RxBus
import ru.noties.debug.Debug
import rx.Observable

class LocalPlayer(
        context: Context
) : BasePlayer(context) {

    val queue: PlaybackQueue = PlaybackQueue()
//    var server: WebSocketMessageServer? = null

    init {
        val playerEvents = playerSubject.distinctUntilChanged()

        playerEvents.filter { it == PlayerEvent.ENDED }
                .concatMap { if (repeat) restart() else prepareNextSong().flatMap { start() } }
                .subscribe()

        playerEvents.doOnError {
            Debug.e(it, "Error while playing")
            stop().subscribe()
        }.subscribe()

        //        errorSubject.subscribe {
        //            Debug.e "RxExoPlayer error:"
        //            Debug.e it
        //            stop().subscribe()
        //        }
    }

    fun setQueueAndPlay(requestedQueue: List<Song>, pos: Int, shuffle: Boolean = false): Observable<*> {
        queue.clear()
        queue.addAll(requestedQueue)
        if (shuffle) queue.shuffle()

        return prepare(pos).flatMap { start() }
    }

    fun prepare(song: Song, retriesLeft: Int = 3): Observable<*> {
        var observable = reset()
                .concatMap { super.prepare(song) }
                .doOnError { Debug.e(currentSong.toString()) }
                .cast(Any::class.java)

        if (retriesLeft > 0)
            observable = observable.onErrorResumeNext(
                    queue.moveToNextAsObservable(true)
                            .concatMap { prepare(it, retriesLeft - 1) }
            )

//        if (server.started)
//            observable = observable.concatMap { server.prepareClients(PrepareInfo(song)) }

        return observable
    }

    override fun start(): Observable<PlayerEvent> {
//        if (server.started)
//            return server.startClients()
//                    .concatMap { super.start() }
//        else
            return super.start()
    }

    override fun pause(): Observable<PlayerEvent> {
//        if (server?.started)
//            return super.pause()
//                    .concatMap { server.pauseClients() }

        return super.pause()
    }

    override fun seekTo(msec: Long): Observable<PlayerEvent> {
//        if (server?.started)
//            return super.pause()
//                    .concatMap { super.seekTo(msec) }
//                    .concatMap { server.prepareClients(PrepareInfo.builder()
//                            .song(currentSong)
//                            .seeking(true)
//                            .position(msec)
//                            .build()) }
//                    .concatMap { start() }

        return super.seekTo(msec)
    }

    fun prepare(position: Int): Observable<*> {
        return queue.moveToAsObservable(position)
                .concatMap { prepare(it) }
    }

    fun prepareNextSong(): Observable<*> {
        return queue.moveToNextAsObservable()
                .concatMap { prepare(it) }
    }

    fun preparePrevSong(): Observable<*> {
        return queue.moveToPrevAsObservable()
                .concatMap { prepare(it) }
    }

    override fun startService(context: Context) {
        context.startService(Intent(context, LocalPlayerService::class.java))
    }

    override var shuffle: Boolean = false
        get() = queue.shuffled

    override var repeat: Boolean = false
        get
        set(flag) {
            field = flag
            RxBus.post(EPlayerStateChanged(shuffle, repeat))
        }
}
