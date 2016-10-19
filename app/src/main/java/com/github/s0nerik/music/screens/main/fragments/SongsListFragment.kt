package com.github.s0nerik.music.screens.main.fragments

import android.os.Bundle
import android.view.View
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.Sorter
import com.github.s0nerik.music.adapters.songs.SongItem
import com.github.s0nerik.music.adapters.songs.SongsListAdapter
import com.github.s0nerik.music.base.BaseBoundFragment
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.databinding.FragmentListSongsBinding
import com.github.s0nerik.music.events.EFabActionAvailable
import com.github.s0nerik.music.ext.hide
import com.github.s0nerik.music.ext.show
import com.github.s0nerik.music.players.LocalPlayer
import com.github.s0nerik.rxbus.RxBus
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.fragment_list_songs.*
import org.jetbrains.anko.support.v4.longToast
import ru.noties.debug.Debug
import javax.inject.Inject

class SongsListFragment : BaseBoundFragment<FragmentListSongsBinding>(), SortableFragment {
    override val layoutId = R.layout.fragment_list_songs

    @Inject
    lateinit var collectionManager: CollectionManager

    @Inject
    lateinit var player: LocalPlayer

    private var songs = mutableListOf<SongItem>()
    private var filteredSongs = mutableListOf<SongItem>()

    private var adapter = SongsListAdapter(filteredSongs)

    private lateinit var sorter: Sorter<SongItem>

    init {
        App.comp.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sorter = Sorter(context, R.menu.sort_songs, R.id.songs_sort_title, adapter, filteredSongs, SongItem.SORTER_PROVIDERS, SongItem.BUBBLE_TEXT_PROVIDERS)
        adapter.mode = FlexibleAdapter.MODE_SINGLE
        adapter.initializeListeners(FlexibleAdapter.OnItemClickListener {
            player.setQueueAndPlay(filteredSongs.map { it.song }, it).subscribe()
            adapter.toggleSelection(it)
            true
        })

        player.songChanges()
                .bindToLifecycle(this)
                .subscribe {
                    val index = filteredSongs.map { it.song }.indexOf(it)
                    adapter.toggleSelection(index)
                }

//        RxBus.on(CurrentSongAvailableEvent).bindToLifecycle(this).subscribe(this.&onEvent)
//        RxBus.on(PlaybackStartedEvent).bindToLifecycle(this).subscribe(this.&onEvent)
//        RxBus.on(PlaybackPausedEvent).bindToLifecycle(this).subscribe(this.&onEvent)
//        RxBus.on(ShouldShuffleSongsEvent).bindToLifecycle(this).subscribe(this.&onEvent)
//        RxBus.on(FilterLocalMusicCommand).bindToLifecycle(this).subscribe(this.&onEvent)
//        RxBus.on(CRequestPlaySong).bindToLifecycle(this).subscribe(this.&onEvent)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.adapter = adapter
        recycler.setHasFixedSize(true)

        adapter.setFastScroller(fastScroller, resources.getColor(R.color.md_deep_purple_600, null))

//          fastScroller.hide()
        recycler.hide()
//        progress.show()
        collectionManager.getSongs()
                .bindToLifecycle(this@SongsListFragment)
                .subscribe(
                        { onSongsLoaded(it) },
                        { Debug.e("Error loading songs", it) }
                )
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) {
            RxBus.post(EFabActionAvailable(R.drawable.ic_shuffle_black_24dp, { shuffleAll() } ))
        }
    }

    private fun shuffleAll() {
        if (filteredSongs.size > 0) {
//            RxBus.post(CSetQueueAndPlay(filteredSongs.map { it.song }, 0, true))
        } else {
            longToast(R.string.nothing_to_shuffle)
        }
    }

    private fun onSongsLoaded(loadedSongs: List<Song>) {
        songs.clear()
        songs.addAll(loadedSongs.map { SongItem(it) })

        filteredSongs.clear()
        filteredSongs.addAll(songs)

        updateSongsList()

//        sorter.sortItems()
    }

    private fun updateSongsList() {
        if (songs.size > 0) {
            adapter.notifyDataSetChanged()
            recycler.show()
        } else {
            emptyView.show()
        }
    }

//    protected fun onEvent(event: ShouldShuffleSongsEvent) {
//        shuffleAll()
//    }
//
//    protected fun onEvent(cmd: FilterLocalMusicCommand) {
//        adapter.searchText = cmd.constraint
//        adapter.filterItems(filteredSongs)
//    }
}
