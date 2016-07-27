package com.github.s0nerik.music.di

import com.github.s0nerik.music.screens.splash.StartActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(x: StartActivity)
}