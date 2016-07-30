package com.github.s0nerik.music.adapters.songs

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.PopupMenu
import android.view.View
import butterknife.OnClick
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.databinding.ItemSongsBinding
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import javax.inject.Inject

class SongViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
    private val binding: ItemSongsBinding

    init {
        App.comp.inject(this)
        binding = DataBindingUtil.bind(view)
        binding.contextMenu.onClick { onContextMenuClicked() }
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

    fun onContextMenuClicked() {
        val wrapper = ContextThemeWrapper(context, R.style.AppTheme)
        val menu = PopupMenu(wrapper, binding.contextMenu)

//        player.isSongInQueue(song)
        if (true) {
            menu.inflate(R.menu.songs_popup_in_queue)
        } else {
            menu.inflate(R.menu.songs_popup)
        }

        menu.setOnMenuItemClickListener({
            when (it.itemId) {
                R.id.action_remove_from_queue -> {
                    context.toast(R.string.song_removed_from_queue)
//                    player.removeFromQueue(song)
                    true
                }
                R.id.action_add_to_queue -> {
                    context.toast(R.string.song_added_to_queue)
//                    player.addToQueue(song)
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

        with(binding) {
            val oldFilter = contextMenu.colorFilter
            contextMenu.setColorFilter(context.resources.getColor(R.color.colorAccent, null))
            menu.setOnDismissListener { contextMenu.colorFilter = oldFilter }
            menu.show()
        }
    }

    @OnClick(R.id.container)
    fun onClicked() {
//        RxBus.post(RequestPlaySongCommand(this.song) as Any)
        mAdapter.toggleSelection(adapterPosition)
        updateSelectedState()
    }

    var song: Song? = null
        set(song) {
            field = song!!
            binding.song = field
            updateSelectedState()
        }
}
