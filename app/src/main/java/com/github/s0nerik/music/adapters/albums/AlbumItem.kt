package com.github.s0nerik.music.adapters.albums

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.MediaStoreItem
import com.github.s0nerik.music.data.models.Album

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFilterable

class AlbumItem(var album: Album) : MediaStoreItem<AlbumViewHolder>(), IFilterable {

    override fun getLayoutRes() = R.layout.item_albums

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): AlbumViewHolder {
        return AlbumViewHolder(inflater.inflate(layoutRes, parent, false), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: AlbumViewHolder, position: Int, payloads: List<*>) {
        holder.album = album
    }

    override fun filter(constraint: String): Boolean {
        return album.title.toLowerCase().contains(constraint.toLowerCase()) ||
                album.artistName.toLowerCase().contains(constraint.toLowerCase()) ||
                album.year.toString() == constraint
    }
}
