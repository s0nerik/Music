package com.github.s0nerik.music.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.SeekBar
import org.jetbrains.anko.dip

class LwmSeekBar @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : SeekBar(context, attrs, defStyleAttr) {
    private val progressPermille: Float
        get() = progress / max.toFloat() * 1000f

    private val progressWidth: Float
        get() {
            return dip(ensureHorizontalDp) + (width / 1000f * progressPermille) - (2 * dip(ensureHorizontalDp) / 1000f * progressPermille)
        }

    private val handleRadiusWithContour: Int
        get() = dip(handleRadiusDp + handleContourHeightDp)

    private val handleRadius: Int
        get() = dip(handleRadiusDp)

    private val lineHeight: Int
        get() = dip(lineHeightDp)

    private val lineHeightDp = 4

    private val ensureHorizontalDp = 16

    private val handleRadiusDp = 6
    private val handleContourHeightDp = 2

    private val progressBackgroundPaint = Paint()
    private val progressForegroundPaint = Paint()

    private val handleBackgroundPaint = Paint()
    private val handleForegroundPaint = Paint()

    init {
        progressBackgroundPaint.color = Color.BLACK
        progressBackgroundPaint.alpha = 127
        progressBackgroundPaint.style = Paint.Style.FILL

        progressForegroundPaint.color = Color.WHITE
        progressForegroundPaint.alpha = 127
        progressForegroundPaint.style = Paint.Style.FILL

        handleBackgroundPaint.color = Color.BLACK
        handleBackgroundPaint.alpha = 127
        handleBackgroundPaint.style = Paint.Style.FILL

        handleForegroundPaint.color = Color.WHITE
        handleForegroundPaint.alpha = 255
        handleForegroundPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        val cx = progressWidth
        val cy = handleRadiusWithContour.toFloat() / 2f

        val offsetTop = Math.max(cy - lineHeight / 2f, 0f)

        canvas.drawRect(0f, offsetTop, width.toFloat(), lineHeight.toFloat() + offsetTop, progressBackgroundPaint)
        canvas.drawRect(0f, offsetTop, progressWidth, lineHeight.toFloat() + offsetTop, progressForegroundPaint)

        canvas.drawCircle(cx, cy, handleRadiusWithContour.toFloat(), handleBackgroundPaint)
        canvas.drawCircle(cx, cy, handleRadius.toFloat(), handleForegroundPaint)
    }

    override fun getMinimumHeight() = if (lineHeight > handleRadiusWithContour / 2f) lineHeight else handleRadiusWithContour / 2
}