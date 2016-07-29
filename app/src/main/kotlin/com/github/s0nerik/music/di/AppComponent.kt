package com.github.s0nerik.music.di

import android.content.res.Resources
import com.github.s0nerik.music.adapters.songs.SongViewHolder
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.data.helpers.db.cursor_getters.CursorGetter
import com.github.s0nerik.music.screens.main.MainActivity
import com.github.s0nerik.music.screens.splash.StartActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(x: StartActivity)
    fun inject(x: MainActivity)

    fun inject(x: CursorGetter)

    fun inject(x: SongViewHolder)

    fun getCollectionManager(): CollectionManager
    fun getResources(): Resources
}