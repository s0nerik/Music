package com.github.s0nerik.music.screens.splash;

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.adapters.songs.SongItem
import com.github.s0nerik.music.adapters.songs.SongsListAdapter
import com.github.s0nerik.music.base.BaseActivity
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.tbruyelle.rxpermissions.RxPermissions
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class StartActivity : @Inject BaseActivity() {
    override val layoutId: Int = R.layout.activity_start

    private val songs: MutableList<SongItem> = mutableListOf()
    private val adapter: SongsListAdapter = SongsListAdapter(songs)

    @BindView(R.id.recycler) lateinit var recycler: RecyclerView

    @Inject lateinit var collectionManager: CollectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.comp.inject(this)
        recycler.adapter = adapter

        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .flatMap { collectionManager.initFromMediaStore() }
                .flatMap { collectionManager.getSongs() }
                .bindToLifecycle(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    songs += it.map { SongItem(it) }
                    adapter.notifyDataSetChanged()
                }
    }
}