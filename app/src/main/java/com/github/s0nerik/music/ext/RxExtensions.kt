package com.github.s0nerik.music.ext

import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun Subscription.addTo(subscription: CompositeSubscription): Subscription {
    subscription.add(subscription)
    return this
}