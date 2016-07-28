package com.github.s0nerik.music.di

import com.github.s0nerik.music.data.helpers.db.CursorGetter
import com.github.s0nerik.music.data.models.Album
import com.github.s0nerik.music.data.models.Artist
import com.github.s0nerik.music.data.models.Song
import com.github.s0nerik.music.screens.splash.StartActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(x: Song)
    fun inject(x: Album)
    fun inject(x: Artist)

    fun inject(x: StartActivity)

    fun inject(x: CursorGetter)
}