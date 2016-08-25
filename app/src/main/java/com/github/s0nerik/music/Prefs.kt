package com.github.s0nerik.music

import com.chibatching.kotpref.KotprefModel

object MainPrefs : KotprefModel() {
    var drawerSelection: Int by intPrefVar(default = R.id.local_music)
}
