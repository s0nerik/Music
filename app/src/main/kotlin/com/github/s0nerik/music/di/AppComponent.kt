package com.github.s0nerik.music.di

import android.content.res.Resources
import com.github.s0nerik.music.adapters.LocalMusicFragmentsAdapter
import com.github.s0nerik.music.adapters.songs.SongViewHolder
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.data.helpers.db.cursor_getters.CursorGetter
import com.github.s0nerik.music.receivers.PendingIntentReceiver
import com.github.s0nerik.music.screens.main.fragments.ArtistsListFragment
import com.github.s0nerik.music.screens.main.fragments.LocalMusicFragment
import com.github.s0nerik.music.screens.main.fragments.NowPlayingFragment
import com.github.s0nerik.music.screens.main.fragments.SongsListFragment
import com.github.s0nerik.music.screens.splash.SplashActivity
import com.github.s0nerik.music.services.LocalPlayerService
import com.github.s0nerik.music.ui.notifications.NowPlayingNotification
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    // Activities
    fun inject(x: SplashActivity)
//    fun inject(x: MainActivity)

    // Fragments
    fun inject(x: LocalMusicFragment)
    fun inject(x: SongsListFragment)
    fun inject(x: ArtistsListFragment)
    fun inject(x: NowPlayingFragment)

    // Adapters
    fun inject(x: LocalMusicFragmentsAdapter)

    fun inject(x: LocalPlayerService)

    fun inject(x: PendingIntentReceiver)

    fun inject(x: NowPlayingNotification)

    fun inject(x: CursorGetter)

    fun inject(x: SongViewHolder)

    fun getCollectionManager(): CollectionManager
    fun getResources(): Resources
}