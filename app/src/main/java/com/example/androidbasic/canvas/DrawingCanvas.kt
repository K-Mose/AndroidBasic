package com.example.androidbasic.canvas

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.DisplayMetrics
import android.view.View

class DrawingCanvas(context: Context ) : View(context){
    lateinit var mPaint: Paint
    lateinit var otherPaint: Paint
    lateinit var outerPaint: Paint
    lateinit var mTextPaint: Paint
    var mPadding: Int? = null
    var arcLeft: Float? = null
    var arcTop: Float? = null
    var arcRight: Float? = null
    var arcBottom: Float? = null
    lateinit var mPath: Path

    init{
        mPaint = Paint().apply{
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = Color.BLUE
            strokeWidth = 10f
        }

        mTextPaint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.YELLOW
            textSize = pxFromDp(context, 24f)
        }
        otherPaint = Paint()
        outerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.YELLOW
        }
        mPadding = 100

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        var screenWidth = displayMetrics.widthPixels
        var screenHeight = displayMetrics.heightPixels
        arcLeft = pxFromDp(context, 20f)
        arcTop = pxFromDp(context, 20f)
        arcRight = pxFromDp(context, 100f)
        arcBottom = pxFromDp(context, 100f)

        var p1 = Point((pxFromDp(context, 80f)+(screenWidth/2) ).toInt() , pxFromDp(context, 40f).toInt())
        var p2 = Point((pxFromDp(context, 40f)+(screenWidth/2) ).toInt() , pxFromDp(context, 80f).toInt())
        var p3 = Point((pxFromDp(context, 120f)+(screenWidth/2) ).toInt() , pxFromDp(context, 80f).toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawLine(250F,250F,600F,600F,mPaint)
        canvas!!.drawLine(600F, 600F, 800F, 600F, mPaint)
        canvas!!.drawText("캔버스 베이직입니당!", (width*0.3).toFloat(), (height*0.8).toFloat(), mTextPaint )

    }

    private fun pxFromDp (context: Context, dp: Float): Float{
        return dp * context.resources.displayMetrics.density
    }
}