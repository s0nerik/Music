package com.github.s0nerik.music.adapters.songs

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.databinding.ItemSongsBinding
import com.github.s0nerik.music.ext.hide
import com.github.s0nerik.music.ext.setAsRingtone
import com.github.s0nerik.music.ext.show
import com.github.s0nerik.music.players.LocalPlayer
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_songs.view.*
import org.jetbrains.anko.onClick
import org.jetbrains.anko.toast
import javax.inject.Inject

class SongViewHolder(
        val big: Boolean,
        inflater: LayoutInflater,
        parent: ViewGroup,
        adapter: FlexibleAdapter<*>
) : FlexibleViewHolder(
        inflater.inflate(R.layout.item_songs, parent, false),
        adapter
) {
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var player: LocalPlayer

    private val binding: ItemSongsBinding

    init {
        App.comp.inject(this)
        binding = DataBindingUtil.bind(itemView)
        binding.big = big
        itemView.contextMenu.onClick { onContextMenuClicked() }
//        itemView.container.onClick { onClicked() }
    }

    fun updateSelectedState() {
        with(binding) {
            if (mAdapter.isSelected(adapterPosition)) {
                vuMeter.show()
            } else {
                vuMeter.hide()
            }
        }
    }

    fun onContextMenuClicked() {
        val wrapper = ContextThemeWrapper(context, R.style.AppTheme)
        val menu = PopupMenu(wrapper, binding.contextMenu)

        if (player.queue.contains(song)) {
            menu.inflate(R.menu.songs_popup_in_queue)
        } else {
            menu.inflate(R.menu.songs_popup)
        }

        menu.setOnMenuItemClickListener({
            when (it.itemId) {
                R.id.action_remove_from_queue -> {
                    context.toast(R.string.song_removed_from_queue)
                    player.queue.remove(song!!)
                    true
                }
                R.id.action_add_to_queue -> {
                    context.toast(R.string.song_added_to_queue)
                    player.queue.add(song!!)
                    true
                }
                R.id.set_as_ringtone -> {
                    song!!.setAsRingtone(context)
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

//    fun onClicked() {
////        RxBus.post(CRequestPlaySong(song!!))
//        mAdapter.toggleSelection(adapterPosition)
//        updateSelectedState()
//    }

    var song: Song? = null
        set(song) {
            field = song!!
            binding.song = field
            updateSelectedState()
        }
}
