package com.dev.quoc.androidtutorials.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dev.quoc.androidtutorials.R
import com.dev.quoc.androidtutorials.TicTacToeJava

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by quocnguyenp. on 12/1/17.
 */
class TicTacToeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {

    companion object {
        private const val X_PARTITION_RADIO = 1 / 3f
        private const val Y_PARTITION_RADIO = 1 / 3f
        private const val COUNT = 3

        private const val moveX = "X"
        private const val moveY = "O"
    }

    private val paint = Paint()
    private val textPaint = Paint()
    private val highLightPaint = Paint()
    private val path = Path()
    private lateinit var squares: Array<Array<Rect>>
    private lateinit var squareData: Array<Array<String>>

    var winCoordinates: Array<TicTacToeJava.SquareCoordinates> = Array(3, { TicTacToeJava.SquareCoordinates(-1, -1) })
    var shouldAnimate = false

    var squarePressListener: SquarePressedListener? = null

    private var rectIndex = Pair(0, 0)
    private var touching: Boolean = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Math.min(measuredHeight, measuredWidth)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init()
    }

    private fun init() {
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 5

        textPaint.color = paint.color
        textPaint.isAntiAlias = true
        //textPaint.typeface = ResourcesCompat.getFont(context, R.font.nunito)
        textPaint.textSize = resources.displayMetrics.scaledDensity * 70

        highLightPaint.color = ContextCompat.getColor(context, R.color.ripple_material_light)
        highLightPaint.style = Paint.Style.FILL
        highLightPaint.isAntiAlias = true

        initializeTicTacToeSquares()
    }

    private fun initializeTicTacToeSquares() {
        squares = Array(3, { Array(3, { Rect() }) })
        squareData = Array(3, { Array(3, { "" }) })

        val xUnit = (width * X_PARTITION_RADIO).toInt() // one init on x-axis
        val yUnit = (width * Y_PARTITION_RADIO).toInt() // one unit on y-axis

        for (j in 0 until COUNT) {
            for (i in 0 until COUNT) {
                squares[i][j] = Rect(i * xUnit, j * yUnit, (i + 1) * xUnit, (j + 1) * yUnit)
            }
        }
    }

    private fun drawTextInsideRectangle(canvas: Canvas?, rect: Rect, str: String) {
        val xOffset = textPaint.measureText(str) * 0.5f
        val yOffset = textPaint.fontMetrics.ascent * -0.4f

        val textX = (rect.exactCenterX()) - xOffset
        val textY = (rect.exactCenterY()) + yOffset

        canvas?.drawText(str, textX, textY, textPaint)
    }

    private fun drawVerticalLines(canvas: Canvas?) {
        // width/3, 0
        canvas?.drawLine(width * X_PARTITION_RADIO, 0f, width * X_PARTITION_RADIO,
                height.toFloat(), paint)

        // width * 2/3, 0
        canvas?.drawLine(width * (2 * X_PARTITION_RADIO), 0f, width * (2 * X_PARTITION_RADIO),
                height.toFloat(), paint)
    }

    private fun drawHorizontalLines(canvas: Canvas?) {
        canvas?.drawLine(0f, height * Y_PARTITION_RADIO, width.toFloat(),
                height * Y_PARTITION_RADIO, paint)

        canvas?.drawLine(0f, height * (2 * Y_PARTITION_RADIO), width.toFloat(),
                height * (2 * Y_PARTITION_RADIO), paint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawVerticalLines(canvas)
        drawHorizontalLines(canvas)
        drawSquareStates(canvas)
        if (shouldAnimate) {
            canvas?.drawPath(path, paint)
        }
        if (touching) {
            drawHighlightRectangle(canvas!!)
        }
    }

    private fun drawHighlightRectangle(canvas: Canvas) {
        canvas.drawRect(squares[rectIndex.first][rectIndex.second], highLightPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                rectIndex = getRectIndexesFor(x, y)
                touching = true
                invalidate(squares[rectIndex.first][rectIndex.second])
            }

            MotionEvent.ACTION_HOVER_MOVE -> {

            }

            MotionEvent.ACTION_UP -> {
                touching = false
                invalidate(squares[rectIndex.first][rectIndex.second])
                val (finalX1, finalY1) = getRectIndexesFor(x, y)
                if ((finalX1 == rectIndex.first) && (finalY1 == rectIndex.second)) {
                    // If initial touch and final touch is in same rectangle or not
                    squarePressListener?.onSquarePressed(rectIndex.first, rectIndex.second)
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                touching = false
            }
        }
        return true
    }

    private fun getRectIndexesFor(x: Float, y: Float): Pair<Int, Int> {
        squares.forEachIndexed { i, rects ->
            for ((j, rect) in rects.withIndex()) {
                if (rect.contains(x.toInt(), y.toInt())) {
                    return Pair(i, j)
                }
            }
        }
        return Pair(-1, -1) // x, y do not lie in our view
    }

    interface SquarePressedListener {
        fun onSquarePressed(i: Int, j: Int)
    }

    private fun drawSquareStates(canvas: Canvas?) {
        for ((i, textArray) in squareData.withIndex()) {
            for ((j, text) in textArray.withIndex()) {
                if (text.isNotEmpty()) {
                    drawTextInsideRectangle(canvas, squares[i][j], text)
                }
            }
        }
    }

    private fun animateWin() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 600
        valueAnimator.addUpdateListener(this)
        valueAnimator.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val measure = PathMeasure(path, false)
        val phase = measure.length - (measure.length * (animation.animatedValue as Float))
        paint.pathEffect = createPathEffect(measure.length, phase, 0.0f)
        invalidate()
    }

    private fun createPathEffect(pathLength: Float, phase: Float, offset: Float): PathEffect {
        return DashPathEffect(floatArrayOf(pathLength, pathLength),
                phase)
    }

    fun drawXAtPosition(x: Int, y: Int) {
        squareData[x][y] = moveX
        invalidate(squares[x][y])
    }

    fun drawOAtPosition(x: Int, y: Int) {
        squareData[x][y] = moveY
        invalidate(squares[x][y])
    }

    fun reset() {
        squareData = Array(3, { Array(3, { "" }) })
        winCoordinates = Array(3, { TicTacToeJava.SquareCoordinates(-1, -1) })
        path.reset()
        shouldAnimate = false
        invalidate()
    }

    fun animateWin(winCoords: Array<TicTacToeJava.SquareCoordinates>) {
        winCoords.forEachIndexed { index, coords ->
            winCoordinates[index] = coords
        }
        if (winCoordinates[0].i < 0) return
        val x1 = squares[winCoordinates[0].i][winCoordinates[0].j].exactCenterX()
        val y1 = squares[winCoordinates[0].i][winCoordinates[0].j].exactCenterY()
        val x2 = squares[winCoordinates[2].i][winCoordinates[2].j].exactCenterX()
        val y2 = squares[winCoordinates[2].i][winCoordinates[2].j].exactCenterY()
        path.reset()
        path.moveTo(x1, y1)
        path.lineTo(x2, y2)
        shouldAnimate = true
        animateWin()
    }
}
