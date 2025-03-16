package com.vk.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class CircularProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = ContextCompat.getColor(context, android.R.color.darker_gray)
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = ContextCompat.getColor(context, android.R.color.holo_blue_light)
        strokeCap = Paint.Cap.ROUND
    }

    private var progress = 0f // Progress in percentage (0-100)
    private val rectF = RectF()

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 100f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = width.coerceAtMost(height) - 40f
        val left = (width - size) / 2
        val top = (height - size) / 2
        rectF.set(left, top, left + size, top + size)

        canvas.drawOval(rectF, backgroundPaint) // Draw background circle
        val sweepAngle = (progress / 100) * 360
        canvas.drawArc(rectF, -90f, sweepAngle, false, progressPaint) // Draw progress
    }
}
