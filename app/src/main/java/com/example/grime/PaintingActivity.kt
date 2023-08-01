package com.example.grime

import android.R.attr.bitmap
import android.R.attr.name
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class PaintingView(context: Context, fileDir: String) : View(context) {
    init {
        // 성능을 위한 하드웨어 모드 설정
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }
    enum class Mode {
        PEN, ERASER
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
    var mode : Mode = Mode.PEN
    var path : Path = Path()
    var bitmap : Bitmap? = null             // 30획을 넘어가는 그림을 저장하기 위한 비트맵
    var bitmapResult : Bitmap? = null       // 기존 비트맵과 30획을 합쳐서 표현할 비트맵
    var lines : ArrayList<Line> = ArrayList<Line>()     // 되돌리기와 취소를 위한 획 리스트
    var canvas : Canvas = Canvas()
    var canvasResult : Canvas = Canvas()
    var undoLines : ArrayList<Line> = ArrayList<Line>()    // 되돌리기와 취소를 위한 취소 획 리스트
    val fileDir = fileDir

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
        bitmapResult = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

        bitmap?.let {
            canvas = Canvas(it)
        }
        bitmapResult?.let {
            canvasResult = Canvas(it)
        }
        // default 배경색으로 하얀색 지정
        canvasResult.drawColor(Color.parseColor("#FFFFFF"))
        loadBitmap()
    }

    override fun onDraw(canvas: Canvas) {
        // 기존 비트맵을 그리고 그 뒤에 되돌리기가 가능한 획들 추가
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }
        for(l in lines) {
            canvas.drawPath(l.path, l.paint)
        }
        canvas.drawPath(path, paint)
        
        // 지우개가 움직이고 있을 때, 지우개를 나타내는 원을 표시
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
                if(lines.size < 30) {
                    lines.add(Line(path, paint))
                }
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
                        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
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
        paint = Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
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

    fun saveBitmap() {
        // 기존 비트맵에 30획의 path를 적용시킨 결과물 비트맵을 저장
        bitmap?.let {
            canvasResult.drawBitmap(it, 0f, 0f, null)
        }
        for(l in lines) {
            canvasResult.drawPath(l.path, l.paint)
        }
        bitmapResult?.setHasAlpha(true);
    }

    fun loadBitmap() {
        try {
            val bitmap = BitmapFactory.decodeFile(fileDir)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
        } catch (e: Exception) {
            Log.e("error", "bitmap is null or open error")
        }

        invalidate()
        Log.i("test", "load bitmap again success")
    }

    fun getPenColor() : Int {
        return penPaint.color
    }
    fun getPenWidth() : Float {
        return penPaint.strokeWidth
    }

    fun setPenColor(color : Int) {
        penPaint.setColor(color)
        paint.color = color
    }

    fun setPenWidth(width : Float) {
        penPaint.setStrokeWidth(width)
        paint.strokeWidth = width
    }

    fun isEraser() : Boolean {
        return mode == Mode.ERASER
    }

    fun isPen() : Boolean {
        return mode == Mode.PEN
    }
}
lateinit var view : PaintingView

class PaintingActivity : AppCompatActivity() {

    lateinit var date : String
    lateinit var paintingbackButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 폰트 적용
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_painting)

        // 배경색 적용
        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        val intent = intent
        date = intent.getStringExtra("date")!!


        var layout = findViewById<LinearLayout>(R.id.paint)
        var undoButton = findViewById<ImageButton>(R.id.undo)
        var redoButton = findViewById<ImageButton>(R.id.redo)
        var penButton = findViewById<ImageButton>(R.id.pen)
        var eraserButton = findViewById<ImageButton>(R.id.eraser)
        var completeButton = findViewById<Button>(R.id.completeButton)
        paintingbackButton = findViewById(R.id.paintBackButton)

        paintingbackButton.setOnClickListener {
            var intent = Intent(this, PaintingDiaryActivity::class.java)
            setResult(RESULT_OK, intent)
            startActivity(intent)
        }

        view = PaintingView(this, cacheDir.path + "/" + date + ".png")


        undoButton.setOnClickListener { view.undo() }
        redoButton.setOnClickListener { view.redo() }
        penButton.setOnClickListener {
            if(view.isPen()) {
                val intent = Intent(this, PenActivity::class.java)
                intent.putExtra("color", view.getPenColor());
                intent.putExtra("width", view.getPenWidth());
                startActivityForResult(intent, 1)
            }
            else
                view.setPenMode()
        }
        eraserButton.setOnClickListener {
            if(view.isEraser()) {
                val intent = Intent(this, EraserActivity::class.java)
                intent.putExtra("size", view.eraserSize);
                startActivityForResult(intent, 2)
            }
            else
                view.setEraserMode()
        }

        completeButton.setOnClickListener {
            view.saveBitmap()
            val storage = cacheDir

            val tempFile = File(storage, date + ".png")

            try {
                tempFile.createNewFile()
                val out = FileOutputStream(tempFile)

                view.bitmapResult?.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.close()

            } catch (e: FileNotFoundException) {
                Log.e("MyTag", "FileNotFoundException : " + e.message)
            } catch (e: IOException) {
                Log.e("MyTag", "IOException : " + e.message)
            } finally {
                saveStatus()
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        layout.addView(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

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
            else if (requestCode == 2) {
                val size = data?.getFloatExtra("size", view.eraserSize)

                if (size != null) {
                    view.eraserSize = size
                }
            }
        }
    }

    fun saveStatus() {
        val file = filesDir.path + "/" + "status.json"
        val loadedData = FileUtil.LoadFile(file)
        var json : JSONObject
        if(loadedData != null)
            json = JSONObject(loadedData)
        else
            json = JSONObject()
        try {
            if (json.getString(date) == "completed" || json.getString(date) == "editing")
                json.put(date, "editing")
            else
                json.put(date, "temp")
        } catch(e: Exception) {
            json.put(date, "temp")
        } finally {
            FileUtil.SaveFile(file, json.toString())
        }
        FileUtil.SaveFile(file, json.toString())
    }
}