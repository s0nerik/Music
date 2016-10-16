package com.github.s0nerik.music.screens.splash;

import android.Manifest
import android.os.Bundle
import com.github.s0nerik.music.App
import com.github.s0nerik.music.base.BaseActivity
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.ext.observeOnMainThread
import com.github.s0nerik.music.screens.main.MainActivity
import com.tbruyelle.rxpermissions.RxPermissions
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.startActivity
import rx.schedulers.Schedulers
import javax.inject.Inject

class SplashActivity : BaseActivity(), AnkoLogger {
    @Inject lateinit var collectionManager: CollectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.comp.inject(this)
    }

    override fun onResume() {
        super.onResume()
        initCollection()
    }

    fun initCollection() {
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .flatMap { collectionManager.initFromMediaStore() }
                .bindToLifecycle(this)
                .subscribeOn(Schedulers.io())
                .observeOnMainThread()
                .subscribe {
                    debug("initFromMediaStore::onNext")
                    startActivity<MainActivity>()
                    finish()
                }
    }
}