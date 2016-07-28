package com.github.s0nerik.music

import android.app.Application
import com.github.s0nerik.music.di.AppComponent
import com.github.s0nerik.music.di.DaggerAppComponent
import ru.noties.debug.Debug

class App : Application() {
    companion object : AppComponent by App.comp {
        lateinit private var comp: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initComponents()

        Debug.init(BuildConfig.DEBUG)
    }

    fun initComponents() {
        comp = DaggerAppComponent.builder().build()
    }
}