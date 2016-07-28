package com.github.s0nerik.music.di

import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.data.helpers.db.cursor_getters.CursorGetter
import com.github.s0nerik.music.screens.splash.StartActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(x: StartActivity)

    fun inject(x: CursorGetter)

    fun getCollectionManager(): CollectionManager
}