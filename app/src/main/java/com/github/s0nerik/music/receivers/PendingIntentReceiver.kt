package com.github.s0nerik.music.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.github.s0nerik.music.App
import com.github.s0nerik.music.events.EControlButton
import com.github.s0nerik.music.services.LocalPlayerService
import com.github.s0nerik.music.ui.notifications.NowPlayingNotification
import com.github.s0nerik.rxbus.RxBus

class PendingIntentReceiver : BroadcastReceiver() {
    init {
        App.comp.inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            NowPlayingNotification.ACTION_CLOSE -> context.stopService(Intent(context, LocalPlayerService::class.java))
            NowPlayingNotification.ACTION_PREV -> RxBus.post(EControlButton(EControlButton.Type.PREV))
            NowPlayingNotification.ACTION_PLAY_PAUSE -> RxBus.post(EControlButton(EControlButton.Type.TOGGLE_PAUSE))
            NowPlayingNotification.ACTION_NEXT -> RxBus.post(EControlButton(EControlButton.Type.NEXT))
        }
    }
}
