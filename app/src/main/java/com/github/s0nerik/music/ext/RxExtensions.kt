package com.github.s0nerik.music.ext

import rx.Observable
import rx.android.schedulers.AndroidSchedulers

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return observeOn(AndroidSchedulers.mainThread())
}