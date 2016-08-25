package com.github.s0nerik.music.adapters.albums

import android.databinding.DataBindingUtil
import android.view.View
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.databinding.ItemArtistAlbumsBinding
import com.github.s0nerik.music.databinding.ItemRecyclerArtistAlbumsBinding
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class ArtistAlbumViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
    private val binding: ItemRecyclerArtistAlbumsBinding

    init {
        binding = DataBindingUtil.bind(view)
    }

    var album: Album? = null
        set(album) {
            field = album!!
            binding.album = field
//            binding.item = mAdapter.getItem(adapterPosition)
//            binding.parent = mAdapter.getExpandableOf(binding.item)
        }
}
