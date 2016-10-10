package com.github.s0nerik.music.adapters.playback_songs

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.view.ViewPager
import android.view.View
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.ViewPagerAdapter
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.databinding.ItemPlaybackSongBinding
import org.jetbrains.anko.layoutInflater

class PlaybackSongItem(val song: Song)

class PlaybackSongsListAdapter(
        val context: Context,
        val items: List<PlaybackSongItem>
) : ViewPagerAdapter() {
    override fun getCount() = items.size

    override fun getView(position: Int, pager: ViewPager): View {
        val binding = DataBindingUtil.inflate<ItemPlaybackSongBinding>(context.layoutInflater, R.layout.item_playback_song, pager, false)
        binding.song = items[position].song
        binding.root.tag = position
        return binding.root
    }

    override fun getItemPosition(`object`: Any?): Int {
        return POSITION_NONE
    }
}
