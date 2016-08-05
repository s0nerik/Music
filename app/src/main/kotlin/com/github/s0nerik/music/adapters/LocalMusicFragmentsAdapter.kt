package com.github.s0nerik.music.adapters

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.github.s0nerik.music.App
import com.github.s0nerik.music.R
import com.github.s0nerik.music.screens.main.fragments.SongsListFragment
import javax.inject.Inject

class LocalMusicFragmentsAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    @Inject
    lateinit var resources: Resources

    private val names: Array<String>
    private val fragments = arrayOf(
            SongsListFragment()
//            SongsListFragment(),
//            ArtistsListFragment(),
//            AlbumsListFragment()
    )

    init {
        App.comp.inject(this)
        names = resources.getStringArray(R.array.local_music_tabs)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return names[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return 1
//        return names.size
    }
}
