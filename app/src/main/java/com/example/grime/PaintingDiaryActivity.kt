package com.example.grime

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.Exception
import java.nio.Buffer
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path


class PaintingDiaryActivity : AppCompatActivity() {

    lateinit var year : String
    lateinit var month : String
    lateinit var date : String
    lateinit var paint : ImageView
    lateinit var titleEdit : EditText
    lateinit var paintingDiaryBackButton: ImageButton
    lateinit var mindButton : ImageButton
    lateinit var writeTextView : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)

        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_painting_diary)


        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)


        //메뉴 버튼 누르면 아래에서 나옴
        val menubutton = findViewById<ImageButton>(R.id.menuButton)
        menubutton.setOnClickListener {
            val bottommenu = bottommenu()
            bottommenu.show(supportFragmentManager, bottommenu.tag)
        }


        val intent = intent
        var diaryDate = findViewById<TextView>(R.id.diaryDate)
        paint = findViewById<ImageView>(R.id.paint)
        writeTextView = findViewById<TextView>(R.id.writeTextView)
        mindButton = findViewById(R.id.mindButton)

        year = intent.getIntExtra("year", 0).toString()
        month = intent.getIntExtra("month", 0).toString()
        date = intent.getIntExtra("date", 0).toString()

        titleEdit = findViewById(R.id.titleEdit)
        paintingDiaryBackButton = findViewById(R.id.paintingdiarybackButton)
        loadCache()

        diaryDate.setText(year + "년 " + month + "월 " + date + "일")


        paint.setOnClickListener {
            val intent = Intent(this, PaintingActivity::class.java)
            intent.putExtra("file", year + "_" + month + "_" + date + ".png");
            startActivityForResult(intent, 1)
        }


        //뒤로가기 버튼
        paintingDiaryBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        mindButton.setOnClickListener {
            val intent = Intent(this, MindActivity::class.java)
            intent.putExtra("date", year + "_" + month + "_" + date);
            startActivityForResult(intent, 2)
        }

        writeTextView.setOnClickListener {
            val intent = Intent(this, WritingActivity::class.java)
            intent.putExtra("date", year + "_" + month + "_" + date);
            startActivityForResult(intent, 3)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                loadCachePainting()
            }
            else if(requestCode == 2) {
                loadCacheMind()
            }
            else if(requestCode == 3) {
                loadCacheContent()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save() {
        savePainting()
        saveMind()
        saveContent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePainting() {
        try {
            val fileName = year + "_" + month + "_" + date + ".png"
            Files.move(Path(cacheDir.path + "/" + fileName), Path(filesDir.path + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveMind() {
        try {
            val fileName = year + "_" + month + "_" + date + "_mind.json"
            Files.move(Path(cacheDir.path + "/" + fileName), Path(filesDir.path + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveContent() {
        try {
            val fileName = year + "_" + month + "_" + date + "_cntent.txt"
            Files.move(Path(cacheDir.path + "/" + fileName), Path(filesDir.path + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    fun loadCache() {
        loadCachePainting()
        loadCacheMind()
        loadCacheContent()
    }

    fun loadCachePainting() {
        try {
            val fileName = year + "_" + month + "_" + date + ".png"
         val bitmap = BitmapFactory.decodeFile(cacheDir.path + "/" + fileName)
          paint.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    fun loadCacheMind() {
        try {
            val key = year + "_" + month + "_" + date
            val file = cacheDir.path + "/" + "mind.json"
            val loaedFile = FileUtil.LoadFile(file)
            val json = JSONObject(loaedFile)
            val mind = json.getString(key)
            if (mind == "angry")
                mindButton.setImageResource(R.drawable.angry)
            else if (mind == "sad")
                mindButton.setImageResource(R.drawable.sad)
            else if (mind == "happy")
                mindButton.setImageResource(R.drawable.happy)
            else if (mind == "soso")
                mindButton.setImageResource(R.drawable.soso)
            else if (mind == "delight")
                mindButton.setImageResource(R.drawable.delight)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    fun loadCacheContent() {
        try {
            val file = cacheDir.path + "/" + year + "_" + month + "_" + date + "_content" + ".txt"
            val loaedFile = FileUtil.LoadFile(file)
            writeTextView.setText(loaedFile)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }
}