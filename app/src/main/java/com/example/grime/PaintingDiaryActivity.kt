package com.example.grime

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class PaintingDiaryActivity : AppCompatActivity() {

    lateinit var year : String
    lateinit var month : String
    lateinit var date : String
    lateinit var paint : ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_diary)

        val intent = intent

        var diaryDate = findViewById<TextView>(R.id.diaryDate)
        paint = findViewById<ImageView>(R.id.paint)

        year = intent.getIntExtra("year", 0).toString()
        month = intent.getIntExtra("month", 0).toString()
        date = intent.getIntExtra("date", 0).toString()


        diaryDate.setText(year + "년 " + month + "월 " + date + "일")


        paint.setOnClickListener {
            val intent = Intent(this, PaintingActivity::class.java)
            intent.putExtra("file", year + "_" + month + "_" + date + ".png");
            startActivityForResult(intent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // 데이터 받기
                val fileName = year + "_" + month + "_" + date + ".png"
                val bitmap = BitmapFactory.decodeFile(cacheDir.path + "/" + fileName)
                paint.setImageBitmap(bitmap)
            }
        }
    }
}