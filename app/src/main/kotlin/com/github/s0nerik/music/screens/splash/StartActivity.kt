package com.github.s0nerik.music.screens.splash;

import android.Manifest
import android.os.Bundle
import com.github.s0nerik.music.App
import com.github.s0nerik.music.base.BaseActivity
import com.github.s0nerik.music.data.helpers.CollectionManager
import com.github.s0nerik.music.screens.main.MainActivity
import com.tbruyelle.rxpermissions.RxPermissions
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import org.jetbrains.anko.intentFor
import rx.schedulers.Schedulers
import javax.inject.Inject

class StartActivity : @Inject BaseActivity() {
    @Inject lateinit var collectionManager: CollectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.comp.inject(this)
        RxPermissions.getInstance(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .flatMap { collectionManager.initFromMediaStore() }
                .bindToLifecycle(this)
                .subscribeOn(Schedulers.io())
                .subscribe {
                    startActivity(intentFor<MainActivity>())
                    finish()
                }
    }
}