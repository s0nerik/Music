package com.github.s0nerik.music.ui.notifications

import android.app.Notification
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.players.LocalPlayer
import com.github.s0nerik.music.receivers.PendingIntentReceiver
import javax.inject.Inject

class NowPlayingNotification(private val song: Song) {
    @Inject
    lateinit var player: LocalPlayer
    @Inject
    lateinit var contentResolver: ContentResolver
    @Inject
    lateinit var resources: Resources
    @Inject
    lateinit var context: Context

    private val closeIntent: PendingIntent
    private val prevIntent: PendingIntent
    private val playPauseIntent: PendingIntent
    private val nextIntent: PendingIntent

    init {
        App.comp.inject(this)

        closeIntent = PendingIntent.getBroadcast(context, 0, createIntent(context, ACTION_CLOSE), PendingIntent.FLAG_UPDATE_CURRENT)
        prevIntent = PendingIntent.getBroadcast(context, 1, createIntent(context, ACTION_PREV), PendingIntent.FLAG_UPDATE_CURRENT)
        playPauseIntent = PendingIntent.getBroadcast(context, 2, createIntent(context, ACTION_PLAY_PAUSE), PendingIntent.FLAG_UPDATE_CURRENT)
        nextIntent = PendingIntent.getBroadcast(context, 3, createIntent(context, ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun create(isPlaying: Boolean): Notification {

        // Build notification
        val builder = Notification.Builder(context).setPriority(NotificationCompat.PRIORITY_HIGH).setOngoing(true)

//        //        if (playing) {
//        //            builder.setSmallIcon(R.drawable.ic_stat_av_play)
//        //        } else {
//        //            builder.setSmallIcon(R.drawable.ic_stat_av_pause)
//        //        }
//
//        builder.setSmallIcon(R.drawable.ic_notification)
//
//        builder.setContentIntent(mainPendingIntent)
        val notification = builder.build()

//        val cover = scaledCover
//
//        // Set default content view
//        notification.contentView = getSmallContentView(cover)
//        notification.bigContentView = getBigContentView(cover)
//
//        if (isPlaying) {
//            notification.contentView.setImageViewResource(R.id.btn_play_pause, R.drawable.ic_av_pause)
//            notification.bigContentView.setImageViewResource(R.id.btn_play_pause, R.drawable.ic_av_pause)
//        } else {
//            notification.contentView.setImageViewResource(R.id.btn_play_pause, R.drawable.ic_av_play_arrow)
//            notification.bigContentView.setImageViewResource(R.id.btn_play_pause, R.drawable.ic_av_play_arrow)
//        }


        return notification
    }

    private fun getBigContentView(cover: Bitmap): RemoteViews {
        val bigContentView = RemoteViews(context.packageName, R.layout.notif_now_playing_big)

//        val palette = Palette.generate(cover)
//        val swatch = PaletteHelper.invokeMethod("getFirstSwatch", arrayOf<Any>(palette))
//
//        if (swatch != null) {
//            // Set background
//            bigContentView.setInt(R.id.background, "setBackgroundColor", swatch!!.rgb)
//
//            // Set button colors
//            val buttonColor = Utils.invokeMethod("stripAlpha", arrayOf<Any>(swatch!!.titleTextColor))
//
//            bigContentView.setInt(R.id.btn_prev, "setColorFilter", buttonColor)
//            bigContentView.setInt(R.id.btn_play_pause, "setColorFilter", buttonColor)
//            bigContentView.setInt(R.id.btn_next, "setColorFilter", buttonColor)
//            bigContentView.setInt(R.id.btn_close, "setColorFilter", buttonColor)
//
//            // Set text colors
//            val textColor = swatch!!.titleTextColor
//
//            bigContentView.setTextColor(R.id.title, textColor)
//            bigContentView.setTextColor(R.id.artist, textColor)
//            bigContentView.setTextColor(R.id.album, textColor)
//        }
//
//
//        bigContentView.setTextViewText(R.id.title, song.title)
//        bigContentView.setTextViewText(R.id.artist, utils!!.invokeMethod("getArtistName", arrayOf<Any>(song.artistName)))
//        bigContentView.setTextViewText(R.id.album, song.albumName)
//
//        bigContentView.setImageViewBitmap(R.id.album_art, cover)
//
//        bigContentView.setOnClickPendingIntent(R.id.btn_play_pause, playPauseIntent)
//        bigContentView.setOnClickPendingIntent(R.id.btn_next, nextIntent)
//        bigContentView.setOnClickPendingIntent(R.id.btn_prev, prevIntent)
//        bigContentView.setOnClickPendingIntent(R.id.btn_close, closeIntent)

        return bigContentView
    }

    private fun getSmallContentView(cover: Bitmap): RemoteViews {
        val contentView = RemoteViews(context.packageName, R.layout.notif_now_playing)

//        contentView.setImageViewBitmap(R.id.album_art, cover)
//
//        val palette = Palette.generate(cover)
//        val swatch = PaletteHelper.invokeMethod("getFirstSwatch", arrayOf<Any>(palette))
//
//        if (swatch != null) {
//            // Set background
//            contentView.setInt(R.id.background, "setBackgroundColor", swatch!!.rgb)
//
//            // Set button colors
//            val buttonColor = Utils.invokeMethod("stripAlpha", arrayOf<Any>(swatch!!.titleTextColor))
//
//            contentView.setInt(R.id.btn_play_pause, "setColorFilter", buttonColor)
//            contentView.setInt(R.id.btn_next, "setColorFilter", buttonColor)
//            contentView.setInt(R.id.btn_close, "setColorFilter", buttonColor)
//
//            // Set text colors
//            val textColor = swatch!!.titleTextColor
//
//            contentView.setTextColor(R.id.title, textColor)
//            contentView.setTextColor(R.id.artist, textColor)
//            contentView.setTextColor(R.id.album, textColor)
//        }
//
//
//        contentView.setTextViewText(R.id.title, song.title)
//        contentView.setTextViewText(R.id.artist, utils!!.invokeMethod("getArtistName", arrayOf<Any>(song.artistName)))
//        contentView.setTextViewText(R.id.album, song.albumName)
//
//        contentView.setOnClickPendingIntent(R.id.btn_play_pause, playPauseIntent)
//        contentView.setOnClickPendingIntent(R.id.btn_next, nextIntent)
//        contentView.setOnClickPendingIntent(R.id.btn_close, closeIntent)

        return contentView
    }

//    private // Scale down bitmap not to get binder error
//    val scaledCover: Bitmap
//        get() {
//            var cover: Bitmap
//            try {
//                val stream = contentResolver.openInputStream(song.albumArtUri)
//                stream.use {
//                    cover = BitmapFactory.decodeStream(it)
//                }
//            } catch (ignore: Exception) {
//            }
//
//            cover = cover ?: utils.noCoverBitmap
//            return scaleDownBitmap(cover, ALBUM_ART_SIZE, resources)
//        }

//    private val mainPendingIntent: PendingIntent
//        get() {
//            val intent = Intent(context, LocalPlaybackActivity)
//            val stackBuilder = TaskStackBuilder.create(context)
//            stackBuilder.addParentStack(LocalPlaybackActivity)
//            stackBuilder.addNextIntent(intent)
//
//            val extras = Bundle()
//            extras.putBoolean("from_notification", true)
//
//            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT, extras)
//        }

    companion object {

        private fun createIntent(context: Context, action: String): Intent {
            val intent = Intent(context, PendingIntentReceiver::class.java)
            intent.action = action
            return intent
        }

        private fun scaleDownBitmap(bitmap: Bitmap, newHeight: Int, res: Resources): Bitmap {
            var bitmap = bitmap

            val densityMultiplier = res.displayMetrics.density

            val h = (newHeight.toInt() * densityMultiplier).toInt()
            val w = (h * bitmap.width / bitmap.height.toInt().toDouble()).toInt()

            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true)

            return bitmap
        }

        val NOTIFICATION_ID = 505173
        val ACTION_CLOSE = "app.player.close"
        val ACTION_PLAY_PAUSE = "app.player.play_pause"
        val ACTION_PREV = "app.player.prev"
        val ACTION_NEXT = "app.player.next"
        val ALBUM_ART_SIZE = 120
    }
}
