package com.github.s0nerik.music.ui.views

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.MenuRes
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import com.github.s0nerik.music.R

class TintableMenuToolbar : Toolbar {

    private var menuItemTintColor: Int = 0

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        val ctx = context
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.TintableMenuToolbar)
        try {
            menuItemTintColor = a.getColor(R.styleable.TintableMenuToolbar_menuItemTint, Color.BLACK)
        } finally {
            a.recycle()
        }
    }

    override fun setNavigationIcon(icon: Drawable?) {
        super.setNavigationIcon(applyTint(icon!!))
    }

    override fun inflateMenu(@MenuRes resId: Int) {
        super.inflateMenu(resId)
        val menu = menu
        for (i in 0..menu.size() - 1) {
            val item = menu.getItem(i)
            val icon = item.icon
            if (icon != null) {
                item.icon = applyTint(icon)
            }
        }
    }

    private fun applyTint(icon: Drawable): Drawable {
        icon.setColorFilter(menuItemTintColor, PorterDuff.Mode.SRC_IN)
        return icon
    }
}
