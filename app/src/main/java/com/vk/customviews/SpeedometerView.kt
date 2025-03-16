package com.vk.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin

import android.animation.ArgbEvaluator


class SpeedometerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {



    // creating a paint for  background
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = ContextCompat.getColor(context, android.R.color.darker_gray)
    }

    // creating a paint progress indication
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        strokeCap = Paint.Cap.ROUND
    }

    // creating a paint for arrow
    private val arrowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, android.R.color.black)
    }

    private var speed = 0f // Speed in km/h
    private val rectF = RectF()
    private var currentColor = ContextCompat.getColor(context, android.R.color.holo_green_light)

    fun setSpeed(value: Float) {
        val animator = ValueAnimator.ofFloat(speed, value.coerceIn(0f, 180f))
        animator.duration = 500
        animator.addUpdateListener {
            speed = it.animatedValue as Float
            animateProgressColor()
            invalidate()
        }
        animator.start()
    }

    private fun animateProgressColor() {
        val targetColor = when {
            speed < 40 -> ContextCompat.getColor(context, android.R.color.holo_green_light)
            speed > 100 -> ContextCompat.getColor(context, android.R.color.holo_red_light)
            else -> ContextCompat.getColor(context, android.R.color.holo_orange_light)
        }

        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), currentColor, targetColor)
        colorAnimator.duration = 100
        colorAnimator.addUpdateListener {
            currentColor = it.animatedValue as Int
            progressPaint.color = currentColor
            invalidate()
        }
        colorAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = width.coerceAtMost(height) - 40f
        val left = (width - size) / 2
        val top = (height - size) / 2
        rectF.set(left, top, left + size, top + size)

        // Draw speedometer background
        canvas.drawArc(rectF, 180f, 180f, false, backgroundPaint)
        // Draw speed progress
        val sweepAngle = (speed / 180) * 180
        canvas.drawArc(rectF, 180f, sweepAngle, false, progressPaint)
        // Draw arrow needle
        drawNeedle(canvas, size / 2)
    }

    private fun drawNeedle(canvas: Canvas, radius: Float) {
        val angle = Math.toRadians((180 + (speed / 180) * 180).toDouble())
        val centerX = width / 2f
        val centerY = height / 2f + radius / 2
        val needleX = (centerX + radius * 0.8 * cos(angle)).toFloat()
        val needleY = (centerY + radius * 0.8 * sin(angle)).toFloat()

        val path = Path().apply {
            moveTo(centerX - 10, centerY)
            lineTo(centerX + 10, centerY)
            lineTo(needleX, needleY)
            close()
        }
        canvas.drawPath(path, arrowPaint)
    }
}