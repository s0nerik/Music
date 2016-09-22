package com.github.s0nerik.music.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatActivity

abstract class BaseActivity : RxAppCompatActivity() {
    protected open val layoutId: Int?
        get() = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId != null) setContentView(layoutId!!)
//        ButterKnife.bind(this)
    }
}

abstract class BaseBoundActivity<out T : ViewDataBinding> : RxAppCompatActivity() {
    protected open val layoutId: Int?
        get() = null

    private lateinit var innerBinding: T
    protected val binding: T by lazy { innerBinding }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutId != null) innerBinding = DataBindingUtil.setContentView(this, layoutId!!)
    }
}