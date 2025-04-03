package com.nemesis.empractice

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.os.bundleOf
import kotlin.random.Random

private const val FILL_STEP = 0.1f
private const val FILL_MAX_VALUE = 1f
private const val FILL_ANIMATION_DURATION_MILLIS = 200L
private const val FILL_PROGRESS_KEY = "fill_progress"
private const val FILL_ORIENTATION_KEY = "fill_orientation"
private const val COLOR_CHANGE_ANIMATION_DURATION_MILLIS = 1500L
private const val RANDOM_COLORS_COUNT = 3
private const val SUPER_STATE_KEY = "super_state"

class FillView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var width = 0f
    private var height = 0f

    private var fillProgress = 0f
    private var fillOrientation = FillOrientation.VERTICAL
    private val fillAnimator = ValueAnimator()

    private val paint = Paint()
    private val colorAnimator = ValueAnimator.ofArgb(*getRandomColors())

    private val gesturesDetector: GestureDetector =
        GestureDetector(getContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                performLongClick()
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                performClick()
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }
        })

    init {
        configureColorAnimator()
        configureFillAnimator()
    }

    private fun configureColorAnimator() {
        colorAnimator.duration = COLOR_CHANGE_ANIMATION_DURATION_MILLIS

        colorAnimator.addUpdateListener {
            paint.color = it.animatedValue as Int
            invalidate()
        }

        val evaluator = ArgbEvaluator()
        colorAnimator.doOnEnd {
            val nextColors = getRandomColors(paint.color)
            colorAnimator.setIntValues(*nextColors)
            colorAnimator.setEvaluator(evaluator)
            colorAnimator.start()
        }
    }

    private fun configureFillAnimator() {
        fillAnimator.duration = FILL_ANIMATION_DURATION_MILLIS
        fillAnimator.addUpdateListener {
            fillProgress = it.animatedValue as Float
            invalidate()
        }
    }

    private fun getRandomColors(startColor: Int = getRandomColor()) =
        IntArray(RANDOM_COLORS_COUNT) { if (it == 0) startColor else getRandomColor() }

    private fun getRandomColor() =
        Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        fillAnimator.setFloatValues(fillProgress, 1f)
        if (!colorAnimator.isRunning) {
            colorAnimator.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        fillAnimator.end()
        colorAnimator.cancel()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = measuredWidth.toFloat()
        height = measuredHeight.toFloat()
    }
    
    @SuppressLint("ClickableViewAccessibility") //performClick called in gesture listener
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gesturesDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean {
        progressFill()
        return super.performClick()
    }

    private fun progressFill() {
        val nextFillProgress =
            if (fillProgress < FILL_MAX_VALUE)
                fillProgress + FILL_STEP else 0f

        fillAnimator.setFloatValues(fillProgress, nextFillProgress)
        fillAnimator.start()
    }

    override fun performLongClick(): Boolean {
        toggleFillOrientation()
        return super.performLongClick()
    }

    private fun toggleFillOrientation() {
        val currentFillProgress = fillProgress
        fillAnimator.setFloatValues(fillProgress, 0f)
        fillAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                fillAnimator.removeListener(this)
                fillOrientation = when (fillOrientation) {
                    FillOrientation.VERTICAL -> FillOrientation.HORIZONTAL
                    FillOrientation.HORIZONTAL -> FillOrientation.VERTICAL
                }
                fillAnimator.setFloatValues(0f, currentFillProgress)
                fillAnimator.start()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        fillAnimator.start()
    }

    override fun onSaveInstanceState(): Parcelable {
        return bundleOf(
            FILL_PROGRESS_KEY to fillProgress,
            FILL_ORIENTATION_KEY to fillOrientation.id,
            SUPER_STATE_KEY to super.onSaveInstanceState()
        )
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        var superState: Parcelable? = state
        if (state is Bundle) {
            fillProgress = state.getFloat(FILL_PROGRESS_KEY)
            fillOrientation = FillOrientation.getById(state.getInt(FILL_ORIENTATION_KEY))

            superState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                state.getParcelable(SUPER_STATE_KEY, Parcelable::class.java)
            } else {
                @Suppress("DEPRECATION")
                state.getParcelable(SUPER_STATE_KEY)
            }

        }
        super.onRestoreInstanceState(superState)
    }

    override fun onDraw(canvas: Canvas) {
        val left = 0f

        val top = when (fillOrientation) {
            FillOrientation.VERTICAL -> height - (height * fillProgress)
            FillOrientation.HORIZONTAL -> 0f
        }

        val right = when (fillOrientation) {
            FillOrientation.VERTICAL -> width
            FillOrientation.HORIZONTAL -> width * fillProgress
        }

        val bottom = height

        canvas.drawRect(
            left,
            top,
            right,
            bottom,
            paint
        )
    }

    private enum class FillOrientation(val id: Int) {
        VERTICAL(0), HORIZONTAL(1);

        companion object {
            fun getById(id: Int) = entries.first { it.id == id }
        }

    }
}