package com.github.s0nerik.music.adapters.artists

import android.view.View
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.github.s0nerik.music.data.models.Artist

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.ExpandableViewHolder
import kotlinx.android.synthetic.main.item_artists.view.*
import org.apache.commons.lang3.text.WordUtils

class ArtistViewHolder(view: View, adapter: FlexibleAdapter<*>) : ExpandableViewHolder(view, adapter) {
    var artist: Artist? = null
        set(artist) {
            field = artist!!

            val artistName = artist.name
            val shortName = artistName.replace("\\p{P}|\\p{S}", "")
                    .replace("  ", " ")
                    .split(" ")
                    .map { it[0] }
                    .joinToString("")

            val fontSize = 24 + Math.round(56f / Math.pow(shortName.length.toDouble(), 1.25))

            val drawable = TextDrawable.builder()
                    .beginConfig()
                    .toUpperCase()
                    .fontSize(fontSize.toInt())
                    .endConfig()
                    .buildRound(WordUtils.capitalize(shortName),
                            ColorGenerator.DEFAULT.getColor(artistName))

            with (itemView) {
                title.text = artistName
                subtitle.text = "${artist.numberOfAlbums} albums, ${artist.numberOfSongs} songs"
                imageView.setImageDrawable(drawable)
            }
        }
}
