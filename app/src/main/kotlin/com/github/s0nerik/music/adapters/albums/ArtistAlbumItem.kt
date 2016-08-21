package com.github.s0nerik.music.adapters.albums

import android.view.LayoutInflater
import android.view.ViewGroup

import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.MediaStoreItem
import com.github.s0nerik.music.data.models.Album

import eu.davidea.flexibleadapter.FlexibleAdapter

class ArtistAlbumItem(var album: Album?) : MediaStoreItem<ArtistAlbumViewHolder>() {
    override fun getLayoutRes() = R.layout.item_artists_album

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): ArtistAlbumViewHolder {
        return ArtistAlbumViewHolder(inflater.inflate(layoutRes, parent, false), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: ArtistAlbumViewHolder, position: Int, payloads: List<*>?) {
        holder.album = album!!
    }
}
