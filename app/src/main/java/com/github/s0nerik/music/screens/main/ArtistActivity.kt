package com.github.s0nerik.music.screens.main

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.albums.ArtistAlbumItem
import com.github.s0nerik.music.adapters.albums.ArtistAlbumsAdapter
import com.github.s0nerik.music.adapters.songs.SongItem
import com.github.s0nerik.music.adapters.songs.SongsListAdapter
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.data.models.Artist
import com.github.s0nerik.music.databinding.ActivityArtistBinding
import com.github.s0nerik.music.ext.albums
import com.github.s0nerik.music.ext.songs
import kotlinx.android.synthetic.main.activity_artist.*
import org.jetbrains.anko.startActivity
import org.parceler.Parcels

class ArtistActivity : BaseBoundActivity<ActivityArtistBinding>() {
    override val layoutId = R.layout.activity_artist

    private val albumsList = mutableListOf<ArtistAlbumItem>()
    private val albumsAdapter = ArtistAlbumsAdapter(albumsList)

    private val songsList = mutableListOf<SongItem>()
    private val songsAdapter = SongsListAdapter(songsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.artist = Parcels.unwrap<Artist>(intent?.getParcelableExtra("artist"))

        albumsList.clear()
        albumsList += binding.artist.albums.toBlocking().first().map { ArtistAlbumItem(it) }
        albumsRecycler.adapter = albumsAdapter
        albumsRecycler.setHasFixedSize(true)
        albumsRecycler.isNestedScrollingEnabled = false

        songsList.clear()
        songsList += binding.artist.songs.toBlocking().first().map { SongItem(it, false) }
        songsRecycler.adapter = songsAdapter
        songsRecycler.setHasFixedSize(true)
        songsRecycler.isNestedScrollingEnabled = false
        (songsRecycler.layoutManager as LinearLayoutManager).isAutoMeasureEnabled = true

        songsAdapter.addSelection(0)
    }

    companion object {
        fun start(ctx: Context, artist: Artist) {
            ctx.startActivity<ArtistActivity>("artist" to Parcels.wrap(artist))
        }
    }
}