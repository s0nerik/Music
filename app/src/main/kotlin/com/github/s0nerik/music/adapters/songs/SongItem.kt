package com.github.s0nerik.music.adapters.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.base.MediaStoreItem
import com.github.s0nerik.music.data.models.Song
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFilterable

class SongItem(
        val song: Song
) : MediaStoreItem<SongViewHolder>(song.id), IFilterable {
    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): SongViewHolder {
        return SongViewHolder(inflater.inflate(R.layout.item_songs, parent, false), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: SongViewHolder, position: Int, payloads: List<*>) {
        holder.song = song
    }

    override fun filter(constraint: String): Boolean {
        val lowercaseConstraint = constraint.toLowerCase()
        return song.title.toLowerCase().contains(lowercaseConstraint)
                || song.artistName?.toLowerCase()?.contains(lowercaseConstraint) as Boolean
    }
}
