package com.github.s0nerik.music.screens.main

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.songs.SongItem
import com.github.s0nerik.music.adapters.songs.SongsListAdapter
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.databinding.ActivityAlbumBinding
import com.github.s0nerik.music.ext.songs
import kotlinx.android.synthetic.main.activity_album.*
import org.jetbrains.anko.startActivity
import org.parceler.Parcels

class AlbumActivity : BaseBoundActivity<ActivityAlbumBinding>() {
    override val layoutId = R.layout.activity_album

    private val songsList = mutableListOf<SongItem>()
    private val songsAdapter = SongsListAdapter(songsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.album = Parcels.unwrap<Album>(intent?.getParcelableExtra("album"))

        songsList.clear()
        songsList += binding.album.songs.toBlocking().first().map { SongItem(it, false) }
        songsRecycler.adapter = songsAdapter
        songsRecycler.setHasFixedSize(true)

        songsAdapter.addSelection(0)
    }

    companion object {
        fun start(ctx: Context, album: Album) {
            ctx.startActivity<AlbumActivity>("album" to Parcels.wrap(album))
        }
    }
}
