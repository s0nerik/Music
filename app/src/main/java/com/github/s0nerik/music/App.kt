package com.github.s0nerik.music

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.github.s0nerik.glide_bindingadapter.GlideBindingConfig
import com.github.s0nerik.music.di.AppComponent
import com.github.s0nerik.music.di.AppModule
import com.github.s0nerik.music.di.DaggerAppComponent
import ru.noties.debug.Debug

class App : Application() {
    init {
        comp = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()

        registerGlideConfigs()
    }

    companion object {
        lateinit var comp: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Debug.init(BuildConfig.DEBUG)
        Kotpref.init(this)
    }

    private fun registerGlideConfigs() {
//        GlideBindingConfig.registerProvider("playbackBg", { iv, request ->
//            val old = iv.drawable
//            request.asBitmap().centerCrop()
////                    .skipMemoryCache(true)
//                    .placeholder(old ?: ColorDrawable(ContextCompat.getColor(this, R.color.md_black_1000)))
//                    .error(R.drawable.no_cover)
//                    .crossFade(2500)
//        })

        GlideBindingConfig.registerProvider("song", { iv, request ->
            request.centerCrop()
                    .placeholder(R.color.md_black_1000)
                    .error(R.drawable.no_cover)
        })

        GlideBindingConfig.setDefault("song")
    }
}