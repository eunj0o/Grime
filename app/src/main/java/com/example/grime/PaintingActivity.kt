package com.example.grime

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class PaintingView(context: Context) : View(context) {
    var paint : Paint = Paint()
    var path : Path = Path()
    var paintColor : Long = 0xFF000000
    lateinit var canvas : Canvas
    lateinit var bitmap : Bitmap

    init {
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f,paint)
        canvas.drawPath(path, paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toFloat()
        val y = event.y.toFloat()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.reset()
                path.moveTo(x, y)
            }

            MotionEvent.ACTION_MOVE -> path.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                path.lineTo(x, y)
                canvas.drawPath(path, paint)
                path.reset()
            }
        }
        this.invalidate()
        return true
    }
}

class Painting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting)
        var view = PaintingView(this);
        var layout = findViewById<ConstraintLayout>(R.id.layout1)
        layout.addView(view)
    }
}