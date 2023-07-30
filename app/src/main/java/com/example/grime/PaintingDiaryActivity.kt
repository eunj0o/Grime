package com.example.grime

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

class PaintingDiaryActivity : AppCompatActivity() {

    lateinit var titleText : EditText
    lateinit var writeText : EditText
    lateinit var paintingDiaryBackButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_diary)

        val intent = intent

        var diaryDate = findViewById<TextView>(R.id.diaryDate)
        var paint = findViewById<LinearLayout>(R.id.paint)

        var year = intent.getIntExtra("year", 0).toString()
        var month = intent.getIntExtra("month", 0).toString()
        var date = intent.getIntExtra("date", 0).toString()

        titleText = findViewById(R.id.titleEdit)
        writeText = findViewById(R.id.writeEdit)
        paintingDiaryBackButton = findViewById(R.id.paintingdiarybackButton)


        diaryDate.setText(year + "년 " + month + "월 " + date + "일")


        paint.setOnClickListener {
            val intent = Intent(this, PaintingActivity::class.java)
            startActivity(intent)
        }

        paintingDiaryBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}