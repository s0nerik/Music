package com.github.s0nerik.music.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle.components.support.RxFragment

abstract class BaseFragment : RxFragment() {
    protected abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(layoutId, container, false)
//        ButterKnife.bind(this, view)
        return view
    }
}

abstract class BaseBoundFragment<out T : ViewDataBinding> : RxFragment() {
    protected abstract val layoutId: Int

    private lateinit var innerBinding: T
    protected val binding: T by lazy { innerBinding }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(layoutId, container, false)
        innerBinding = DataBindingUtil.bind(view)
        return view
    }
}