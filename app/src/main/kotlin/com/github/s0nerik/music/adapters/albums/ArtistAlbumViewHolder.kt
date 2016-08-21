package com.github.s0nerik.music.adapters.albums

import android.view.View
import com.github.s0nerik.music.adapters.albums.AlbumViewHolder
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.ext.hide
import com.github.s0nerik.music.ext.show
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.item_artists_album.view.*

class ArtistAlbumViewHolder(view: View, adapter: FlexibleAdapter<*>) : AlbumViewHolder(view, adapter) {
    override var album: Album?
        get() = super.album
        set(album) {
            super.album = album!!
            with (itemView) {
                subtitle.text = if (album.year > 0) "${album.year} • ${album.songsCount} songs" else "${album.songsCount} songs"

                shadowTop.hide()
                shadowBottom.hide()

                val item = mAdapter.getItem(adapterPosition)
                val parent = mAdapter.getExpandableOf(item)

                if (parent?.isExpanded as Boolean) {
                    shadowBottom.show()

                    val childPos = parent.subItems.indexOf(item)
                    if (childPos == 0) {
                        shadowTop.show()
                    }
//                    else if (childPos == parent.subItems.size() - 1) {
//                        shadowBottom.show()
//                    }
                }
            }
        }
}
