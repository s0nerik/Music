package com.github.s0nerik.music.adapters.songs

import android.content.Context
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.PopupMenu
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.s0nerik.music.R
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.ext.artistNameForUi
import com.github.s0nerik.music.ext.durationString
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import javax.inject.Inject

class SongViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
    init {
        ButterKnife.bind(this, view)
    }

    @Inject
    lateinit var context: Context

    fun updateSelectedState() {
        //        if (mAdapter.isSelected(adapterPosition)) {
        //            playIcon.show()
        //        } else {
        //            playIcon.hide()
        //        }
    }

    @OnClick(R.id.contextMenu)
    fun onContextMenuClicked(v: View) {
        val wrapper = ContextThemeWrapper(context, R.style.AppTheme)
        val menu = PopupMenu(wrapper, v)

//        if (player.isSongInQueue(song)) {
//            menu.inflate(R.menu.songs_popup_in_queue)
//        } else {
//            menu.inflate(R.menu.songs_popup)
//        }

        menu.setOnMenuItemClickListener({
            when (it.itemId) {
                R.id.action_remove_from_queue -> {
//                    player!!.invokeMethod("removeFromQueue", arrayOf<Any>(this.song))
//                    val toast = Toast.invokeMethod("makeText", arrayOf<Any>(context, R.string.song_removed_from_queue, Toast.LENGTH_SHORT))
//                    toast.show()
                    true
                }
                R.id.action_add_to_queue -> {
//                    player!!.invokeMethod("addToQueue", arrayOf<Any>(this.song))
//                    val toast = Toast.invokeMethod("makeText", arrayOf<Any>(context, R.string.song_added_to_queue, Toast.LENGTH_SHORT))
//                    toast.show()
//                    return true
                    true
                }
                R.id.set_as_ringtone -> {
//                    Utils.invokeMethod("setSongAsRingtone", arrayOf<Any>(context, this.song))
//                    return true
                    true
                }
                else -> false
            }
        })

        val imageView = v as ImageView
        val oldFilter = imageView.colorFilter
        imageView.setColorFilter(context.resources.getColor(R.color.colorAccent, null))
        menu.setOnDismissListener { imageView.colorFilter = oldFilter }
        menu.show()
    }

    @OnClick(R.id.container)
    fun onClicked() {
//        RxBus.post(RequestPlaySongCommand(this.song) as Any)
        mAdapter.toggleSelection(adapterPosition)
        updateSelectedState()
    }

    @BindView(R.id.title) lateinit var title: TextView
    @BindView(R.id.artist) lateinit var artist: TextView
    @BindView(R.id.duration) lateinit var duration: TextView
    @BindView(R.id.contextMenu) lateinit var contextMenu: ImageView
    @BindView(R.id.container) lateinit var container: RelativeLayout

    var song: Song? = null
        set(song) {
            field = song!!

            title.text = song.title
            artist.text = song.artistNameForUi
            duration.text = song.durationString

            updateSelectedState()
        }
}
