package com.github.s0nerik.music.ui.ext

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

class HorizontalLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        orientation = HORIZONTAL
    }

    override fun getOrientation() = HORIZONTAL
}