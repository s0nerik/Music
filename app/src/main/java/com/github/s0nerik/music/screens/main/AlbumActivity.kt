package com.github.s0nerik.music.screens.main

import android.content.Context
import android.os.Bundle
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.songs.SongItem
import com.github.s0nerik.music.adapters.songs.SongsListAdapter
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.databinding.ActivityAlbumBinding
import com.github.s0nerik.music.ext.hide
import com.github.s0nerik.music.ext.show
import com.github.s0nerik.music.ext.songs
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_album.view.*
import org.jetbrains.anko.onClick
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

//        blurView.setBlurredView(image)

        btnBack.onClick { onBackPressed() }
        btnMenu.onClick {  }
    }

    override fun onResume() {
        super.onResume()

        appBarLayout.addOnOffsetChangedListener { appBarLayout, i ->
//            val radius = Math.sqrt(Math.abs(i.toDouble())).toInt()
            val radius = Math.sqrt(Math.abs(i.toDouble()))
            if (radius > 0) {
                blurView.show()
                blurView.setBlurRadius(radius.toFloat())
                blurView.invalidate()
            } else {
                blurView.hide()
            }
        }
    }

    companion object {
        fun start(ctx: Context, album: Album) {
            ctx.startActivity<AlbumActivity>("album" to Parcels.wrap(album))
        }
    }
}
