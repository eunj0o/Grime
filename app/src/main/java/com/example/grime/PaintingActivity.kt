package com.example.grime

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.LinkedList


class PaintingView(context: Context) : View(context) {
    var paint : Paint = Paint()
    var path : Path = Path()
    var paths : LinkedList<Path> = LinkedList<Path>()
    var undoPaths : LinkedList<Path> = LinkedList<Path>()

    init {
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10f);
        paint.setStyle(Paint.Style.STROKE);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        for(p in paths) {
            canvas.drawPath(p, paint)
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toFloat()
        val y = event.y.toFloat()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                undoPaths.clear()
                path.reset()
                path.moveTo(x, y)
            }

            MotionEvent.ACTION_MOVE -> path.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                paths.add(path)
                path = Path()
            }
        }
        this.invalidate()
        return true
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            undoPaths.add(paths.removeLast())
            invalidate()
        }
    }

    fun redo() {
        if (undoPaths.isNotEmpty()) {
            paths.add(undoPaths.removeLast())
            invalidate()
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

        var layout = findViewById<ConstraintLayout>(R.id.layout2)
        var undoButton = findViewById<Button>(R.id.undo)
        var redoButton = findViewById<Button>(R.id.redo)
        var penButton = findViewById<Button>(R.id.pen)
        view = PaintingView(this)
        undoButton.setOnClickListener { view.undo() }
        redoButton.setOnClickListener { view.redo() }
        penButton.setOnClickListener {
            val intent = Intent(this, PenActivity::class.java)
            intent.putExtra("color", view.getPenColor());
            intent.putExtra("width", view.getPenWidth());
            startActivityForResult(intent, 1)
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