package com.github.s0nerik.music.di

//import app.App
//import app.Config
//import app.Utils
//import app.helpers.CollectionManager
//import app.helpers.StationsExplorer
//import app.helpers.wifi.WifiUtils
//import app.players.LocalPlayer
//import app.players.StreamPlayer
//import app.prefs.MainPrefs
//import app.server.HttpStreamServer
//import app.server.MusicStation
//import app.ui.Blurer
//import app.websocket.WebSocketMessageServer

import com.github.s0nerik.music.App
import com.github.s0nerik.music.data.helpers.CollectionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {
//    @Provides
//    @Singleton
//    fun provideBlurer(): Blurer {
//        return Blurer()
//    }
//
//    @Provides
//    @Singleton
//    fun provideLocalPlayer(): LocalPlayer {
//        return LocalPlayer()
//    }
//
//    @Provides
//    @Singleton
//    fun provideStreamPlayer(): StreamPlayer {
//        return StreamPlayer()
//    }
//
//    @Provides
//    @Singleton
//    fun provideMusicStation(): MusicStation {
//        return MusicStation()
//    }
//
//    @Provides
//    @Singleton
//    fun provideUtils(): Utils {
//        return Utils()
//    }
//
//    @Provides
//    @Singleton
//    fun provideWifiUtils(): WifiUtils {
//        return WifiUtils()
//    }
//
//    @Provides
//    @Singleton
//    fun provideConnectivityManager(): ConnectivityManager {
//        return application.getSystemService(CONNECTIVITY_SERVICE)
//    }
//
//    @Provides
//    @Singleton
//    fun provideWindowManager(): WindowManager {
//        return application.getSystemService(WINDOW_SERVICE)
//    }
//
//    @Provides
//    @Singleton
//    fun provideMainPrefs(): MainPrefs {
//        return MainPrefs.get(application)
//    }
//
//    @Provides
//    @Singleton
//    fun provideStationsExplorer(): StationsExplorer {
//        return StationsExplorer()
//    }
//
//    @Provides
//    @Singleton
//    fun provideHttpStreamServer(): HttpStreamServer {
//        return HttpStreamServer(Config.HTTP_SERVER_PORT)
//    }
//
//    @Provides
//    @Singleton
//    fun provideWebSocketMessageServer(): WebSocketMessageServer {
//        return WebSocketMessageServer(InetSocketAddress(Config.WS_SERVER_PORT))
//    }

    @Provides
    @Singleton
    fun provideCollectionManager(): CollectionManager {
        return CollectionManager()
    }

}