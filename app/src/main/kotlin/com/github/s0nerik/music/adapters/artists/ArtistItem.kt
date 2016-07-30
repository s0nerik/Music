//package com.github.s0nerik.music.adapters.artists
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.github.s0nerik.music.R
//import com.github.s0nerik.music.adapters.MediaStoreExpandableItem
//import com.github.s0nerik.music.data.models.Artist
//
//import eu.davidea.flexibleadapter.FlexibleAdapter
//import eu.davidea.flexibleadapter.items.IFilterable
//
//class ArtistItem(var artist: Artist) : MediaStoreExpandableItem<ArtistViewHolder, ArtistAlbumItem>(), IFilterable {
//
//    val layoutRes = R.layout.item_artists
//
//    fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): ArtistViewHolder {
//        return ArtistViewHolder(inflater.inflate(layoutRes, parent, false), adapter)
//    }
//
//    fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: ArtistViewHolder, position: Int, payloads: List<*>) {
//        holder.artist = artist
//    }
//
//    override fun filter(constraint: String): Boolean {
//        return artist.name.toLowerCase().contains(constraint.toLowerCase())
//    }
//}
