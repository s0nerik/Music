package com.github.s0nerik.music.screens.main

import android.media.AudioManager.*
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.KeyEvent.KEYCODE_VOLUME_UP
import com.github.s0nerik.music.MainPrefs
import com.github.s0nerik.music.R
import com.github.s0nerik.music.base.BaseBoundActivity
import com.github.s0nerik.music.databinding.ActivityMainBinding
import com.github.s0nerik.music.screens.main.fragments.LocalMusicFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.audioManager

class MainActivity : BaseBoundActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        App.comp.inject(this)

//        RxBus.on(Toolbar::class.java).bindToLifecycle(this).subscribe(::onEvent)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initNavigationDrawer()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KEYCODE_VOLUME_UP -> {
                audioManager.adjustStreamVolume(STREAM_MUSIC, ADJUST_RAISE, FLAG_SHOW_UI)
                true
            }
            KEYCODE_VOLUME_DOWN -> {
                audioManager.adjustStreamVolume(STREAM_MUSIC, ADJUST_LOWER, FLAG_SHOW_UI)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    private fun initNavigationDrawer() {
        navigation.setNavigationItemSelectedListener({
            MainPrefs.drawerSelection = it.itemId
            drawerLayout.closeDrawer(Gravity.LEFT)
            showFragmentFromDrawer(it.itemId)
            true
        })

        showFragmentFromDrawer(MainPrefs.drawerSelection)
    }

    private fun showFragmentFromDrawer(@IdRes id: Int) {
        val fragment = when (id) {
            R.id.local_music -> LocalMusicFragment()
//                    R.id.stations_around -> StationsAroundFragment()
            else -> LocalMusicFragment()
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
    }

    // region Event handlers

    private fun onEvent(toolbar: Toolbar) {
        val drawerToggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        )
        drawerLayout.setDrawerListener(drawerToggle)
//            drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT)
        drawerToggle.syncState()
    }

    // endregion
}