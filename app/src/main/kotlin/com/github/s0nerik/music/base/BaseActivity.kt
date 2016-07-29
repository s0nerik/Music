package com.github.s0nerik.music.base

import android.os.Bundle
import butterknife.ButterKnife
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

abstract class BaseActivity : RxAppCompatActivity() {
    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        ButterKnife.bind(this)
    }
}