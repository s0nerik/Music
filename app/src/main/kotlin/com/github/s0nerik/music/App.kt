package com.github.s0nerik.music

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.github.s0nerik.music.di.AppComponent
import com.github.s0nerik.music.di.AppModule
import com.github.s0nerik.music.di.DaggerAppComponent
import ru.noties.debug.Debug

class App : Application() {
    init {
        comp = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    companion object {
        lateinit var comp: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Debug.init(BuildConfig.DEBUG)
        Kotpref.init(this)
    }
}