package com.github.s0nerik.music.adapters.songs

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.MediaStoreItem
import com.github.s0nerik.music.data.models.Song
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFilterable

class SongItem(
        val song: Song,
        val big: Boolean = true
) : MediaStoreItem<SongViewHolder>(song.id), IFilterable {
    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): SongViewHolder {
        return SongViewHolder(big, inflater, parent, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: SongViewHolder, position: Int, payloads: List<*>) {
        holder.song = song
    }

    override fun filter(constraint: String): Boolean {
        val lowercaseConstraint = constraint.toLowerCase()
        return song.title.toLowerCase().contains(lowercaseConstraint)
                || song.artistName.toLowerCase().contains(lowercaseConstraint)
    }

    companion object {
        val SORTER_PROVIDERS = mapOf(
                Pair<Int, (it: SongItem) -> Comparable<*>>(R.id.songs_sort_title, { it.song.title }),
                Pair<Int, (it: SongItem) -> Comparable<*>>(R.id.songs_sort_artist, { it.song.artistName }),
                Pair<Int, (it: SongItem) -> Comparable<*>>(R.id.songs_sort_album, { it.song.albumName })
//                Pair<Int, (it: SongItem) -> Comparable<*>>(R.id.songs_sort_year, { it.song.album.year }),
        )

        val BUBBLE_TEXT_PROVIDERS = mapOf(
                Pair<Int, (it: SongItem) -> String>(R.id.songs_sort_title, { it.song.title[0].toString() }),
                Pair<Int, (it: SongItem) -> String>(R.id.songs_sort_artist, { it.song.artistName[0].toString() }),
                Pair<Int, (it: SongItem) -> String>(R.id.songs_sort_album, { it.song.albumName[0].toString() })
//                Pair<Int, (it: SongItem) -> String>(R.id.songs_sort_year, { it.song.album.year[0..3] })
        )
    }
}
