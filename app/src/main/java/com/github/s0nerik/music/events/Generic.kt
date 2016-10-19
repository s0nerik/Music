package com.github.s0nerik.music.events

import android.support.annotation.DrawableRes

data class EFabActionAvailable(
        @DrawableRes val iconId: Int,
        val action: () -> Unit
)