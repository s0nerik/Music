package com.github.s0nerik.music.screens.main

import android.os.Bundle
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.songs.SongItem
import com.github.s0nerik.music.adapters.songs.SongsListAdapter
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : BaseBoundActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    private val songs: MutableList<SongItem> = mutableListOf()
    private val adapter: SongsListAdapter = SongsListAdapter(songs)

    @Inject lateinit var collectionManager: CollectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.comp.inject(this)
        binding.recycler.adapter = adapter

        songs += collectionManager.getSongs().toBlocking().first().map { SongItem(it) }
        adapter.notifyDataSetChanged()
    }
}