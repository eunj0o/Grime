package com.example.grime

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.LinkedList
import java.util.Stack


class PaintingView(context: Context) : View(context) {
    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }
    enum class Mode {
        MOVE, PEN, ERASER
    }
    var paint : Paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    var penPaint : Paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    var eraserPaint : Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    var eraserSize : Float = 20f
    var isEraserMoving : Boolean = false
    var eraserPosX : Float = 0f
    var eraserPosY : Float = 0f
    var mode : Mode = Mode.MOVE
    var path : Path = Path()
    var bitmap : Bitmap? = null
    var lines : ArrayList<Line> = ArrayList<Line>()
    var canvas : Canvas = Canvas()
    var undoLines : ArrayList<Line> = ArrayList<Line>()

    class Line {
        var path : Path = Path()
        var paint : Paint = Paint()

        constructor(path: Path, paint : Paint) {
            this.path = path
            this.paint = paint
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        bitmap?.let {
            canvas = Canvas(it)
        }
    }

    override fun onDraw(canvas: Canvas) {
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, paint)
        }
        for(l in lines) {
            canvas.drawPath(l.path, l.paint)
        }
        canvas.drawPath(path, paint)
        if(isEraserMoving)
            canvas.drawCircle(eraserPosX, eraserPosY, eraserSize, eraserPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toFloat()
        val y = event.y.toFloat()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                undoLines.clear()
                path.reset()
                path.moveTo(x, y)
            }

            MotionEvent.ACTION_MOVE -> {
                if(mode == Mode.ERASER) {
                    eraserPosX = x
                    eraserPosY = y
                    path.addCircle(x, y, eraserSize, Path.Direction.CW)
                    isEraserMoving = true
                }
                else
                    path.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                if(lines.size < 30)
                    lines.add(Line(path, paint))
                else {
                    val line = lines.removeFirst()
                    canvas.drawPath(line.path, line.paint)
                    lines.add(Line(path, paint))
                }

                path = Path()
                isEraserMoving = false
                eraserPosY = 0f
                eraserPosY = 0f

                if(mode == Mode.ERASER) {
                    paint = Paint().apply {
                        isAntiAlias = true
                        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                    }

                }
                else {
                    paint = Paint().apply {
                        isAntiAlias = penPaint.isAntiAlias
                        color = penPaint.color
                        strokeWidth = penPaint.strokeWidth
                        style = penPaint.style
                    }
                }
            }
        }
        this.invalidate()
        return true
    }

    fun undo() {
        if (lines.isNotEmpty()) {
            undoLines.add(lines.removeLast())
            invalidate()
        }
    }

    fun redo() {
        if (undoLines.isNotEmpty()) {
            lines.add(undoLines.removeLast())
            invalidate()
        }
    }

    fun setEraserMode() {
        mode = Mode.ERASER
        paint = Paint()
        paint.isAntiAlias = true
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    fun setPenMode() {
        mode = Mode.PEN
        paint = Paint().apply {
            isAntiAlias = penPaint.isAntiAlias
            color = penPaint.color
            strokeWidth = penPaint.strokeWidth
            style = penPaint.style
        }
    }

    fun getPenColor() : Int {
        return paint.color
    }
    fun getPenWidth() : Float {
        return paint.strokeWidth
    }

    fun setPenColor(color : Int) {
        paint.setColor(color)
    }

    fun setPenWidth(width : Float) {
        paint.setStrokeWidth(width)
    }
}
lateinit var view : PaintingView

class PaintingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting)

        var layout = findViewById<LinearLayout>(R.id.paint)
        var undoButton = findViewById<ImageButton>(R.id.undo)
        var redoButton = findViewById<ImageButton>(R.id.redo)
        var penButton = findViewById<ImageButton>(R.id.pen)
        var eraserButton = findViewById<ImageButton>(R.id.eraser)
        view = PaintingView(this)
        undoButton.setOnClickListener { view.undo() }
        redoButton.setOnClickListener { view.redo() }
        penButton.setOnClickListener {
            val intent = Intent(this, PenActivity::class.java)
            intent.putExtra("color", view.getPenColor());
            intent.putExtra("width", view.getPenWidth());
            startActivityForResult(intent, 1)
        }
        eraserButton.setOnClickListener {
            view.setEraserMode()
        }

        layout.addView(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // 데이터 받기
                val color = data?.getIntExtra("color", view.getPenColor())
                val width = data?.getFloatExtra("width", view.getPenWidth())

                if (color != null) {
                    view.setPenColor(color)
                }
                if (width != null) {
                    view.setPenWidth(width)
                }
            }
        }
    }

}