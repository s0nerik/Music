package com.github.s0nerik.music.adapters.albums

import android.databinding.DataBindingUtil
import android.view.View
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.databinding.ItemAlbumsBinding
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_albums.view.*
import org.jetbrains.anko.onClick

open class AlbumViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
    private val binding: ItemAlbumsBinding

    init {
        binding = DataBindingUtil.bind(view)
        with (itemView) {
            layout.onClick {
//                context.startActivity<AlbumInfoActivity>("album" to album)
            }
        }
    }

    open var album: Album? = null
        set(album) {
            field = album!!
            binding.album = field
        }
}
