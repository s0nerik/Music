package com.github.s0nerik.music.screens.main.fragments

import android.os.Bundle
import android.view.View
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.Sorter
import com.github.s0nerik.music.adapters.albums.ArtistAlbumItem
import com.github.s0nerik.music.adapters.artists.ArtistItem
import com.github.s0nerik.music.adapters.artists.ArtistsAdapter
import com.github.s0nerik.music.adapters.songs.SongItem
import com.github.s0nerik.music.base.BaseBoundFragment
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.data.models.Artist
import com.github.s0nerik.music.databinding.FragmentListArtistsBinding
import com.github.s0nerik.music.ext.hide
import com.github.s0nerik.music.ext.show

import kotlinx.android.synthetic.main.fragment_list_artists.*

import javax.inject.Inject

class ArtistsListFragment : BaseBoundFragment<FragmentListArtistsBinding>(), SortableFragment {
    override val layoutId = R.layout.fragment_list_artists

    @Inject
    lateinit var collectionManager: CollectionManager

    private lateinit var sorter: Sorter<SongItem>
    private lateinit var adapter: ArtistsAdapter

    private val artists = mutableListOf<ArtistItem>()
    private var unfilteredArtists = listOf<ArtistItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.comp.inject(this)

//        RxBus.on(FilterLocalMusicCommand::class.java)
//                .bindToLifecycle(this)
//                .subscribe(this.&onFilter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArtistsAdapter(artists)
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)

        loadArtists()
    }

    private fun loadArtists() {
        recycler.hide()
        progress.show()
        onArtistsLoaded(collectionManager.getArtists().toBlocking().first())
    }

    private fun onArtistsLoaded(artists: List<Artist>) {
        progress.hide()

        this.artists.clear()
        this.artists.addAll(artists.map {
            val item = ArtistItem(it)
            item.subItems = collectionManager.getAlbums(it).toBlocking().first().map { ArtistAlbumItem(it) }
            item
        })

        unfilteredArtists = this.artists

        if (artists.isNotEmpty()) {
            adapter.notifyDataSetChanged()
            recycler.show()
        } else {
            empty.show()
        }
    }

//    private fun onFilter(cmd: FilterLocalMusicCommand) {
//        adapter.searchText = cmd.constraint
//        adapter.filterItems(unfilteredArtists)
//    }
}
