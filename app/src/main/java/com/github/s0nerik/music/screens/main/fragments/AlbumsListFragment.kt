package com.github.s0nerik.music.screens.main.fragments

import android.os.Bundle
import android.view.View
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.albums.AlbumItem
import com.github.s0nerik.music.adapters.albums.AlbumsAdapter
import com.github.s0nerik.music.base.BaseFragment
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.data.models.Artist
import com.github.s0nerik.music.ext.albums
import com.github.s0nerik.music.ext.hide
import com.github.s0nerik.music.ext.show
import kotlinx.android.synthetic.main.fragment_list_artists.*
import org.jetbrains.anko.support.v4.withArguments
import org.parceler.Parcels
import javax.inject.Inject

class AlbumsListFragment : BaseFragment(), SortableFragment {
    override val layoutId = R.layout.fragment_list_albums

    @Inject
    lateinit var collectionManager: CollectionManager

    private lateinit var adapter: AlbumsAdapter

    private var artist: Artist? = null

    private val albums = mutableListOf<AlbumItem>()
    private var unfilteredAlbums = listOf<AlbumItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.comp.inject(this)
        artist = Parcels.unwrap<Artist>(arguments?.getParcelable("artist"))

//        RxBus.on(FilterLocalMusicCommand)
//                .bindToLifecycle(this)
//                .subscribe(this.&onFilter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AlbumsAdapter(albums)
        recycler.adapter = adapter

//        adapter.initializeListeners(FlexibleAdapter.OnItemClickListener {
//            AlbumActivity.start(activity, albums[it].album)
//            true
//        })

        loadAlbums()
    }

    private fun loadAlbums() {
        recycler.hide()
        progress.show()

        onAlbumsLoaded((if (artist != null) artist!!.albums else collectionManager.getAlbums()).toBlocking().first())
    }

    private fun onAlbumsLoaded(loadedAlbums: List<Album>) {
        progress.hide()

        albums.clear()
        albums.addAll(loadedAlbums.map { AlbumItem(it) })

        unfilteredAlbums = albums

        if (albums.isNotEmpty()) {
            adapter.notifyDataSetChanged()
            recycler.show()
        } else {
            empty.show()
        }
    }

//    private fun onFilter(cmd: FilterLocalMusicCommand) {
//        adapter.searchText = cmd.constraint
//        adapter.filterItems(unfilteredAlbums)
//    }

    companion object {
        fun create(artist: Artist): AlbumsListFragment {
            return AlbumsListFragment().withArguments("artist" to Parcels.wrap(artist))
        }
    }
}
