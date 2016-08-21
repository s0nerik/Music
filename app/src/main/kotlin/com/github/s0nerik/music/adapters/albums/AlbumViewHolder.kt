package com.github.s0nerik.music.adapters.albums

import android.view.View
import com.bumptech.glide.Glide
import com.github.s0nerik.music.R
import com.github.s0nerik.music.data.models.Album
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import kotlinx.android.synthetic.main.item_albums.view.*
import org.jetbrains.anko.onClick

open class AlbumViewHolder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
    init {
        with (itemView) {
            layout.onClick {
//                context.startActivity<AlbumInfoActivity>("album" to album)
            }
        }
    }

    open var album: Album? = null
        set(album) {
            field = album!!

            with(itemView) {
                title.text = album.title
                subtitle.text = album.artistName

                Glide.with(cover.context)
                        .load("file://${album.albumArtPath}")
                        .centerCrop()
                        .error(R.drawable.no_cover)
                        .placeholder(R.color.md_black_1000)
                        .crossFade()
                        .into(cover)
            }
        }
}
