package com.example.grime

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception
import java.nio.Buffer


class PaintingDiaryActivity : AppCompatActivity() {

    lateinit var year : String
    lateinit var month : String
    lateinit var date : String
    lateinit var paint : ImageView
    lateinit var mindButton : ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_diary)

        val intent = intent

        var diaryDate = findViewById<TextView>(R.id.diaryDate)
        paint = findViewById<ImageView>(R.id.paint)
        mindButton = findViewById(R.id.mindButton)

        year = intent.getIntExtra("year", 0).toString()
        month = intent.getIntExtra("month", 0).toString()
        date = intent.getIntExtra("date", 0).toString()


        diaryDate.setText(year + "년 " + month + "월 " + date + "일")


        paint.setOnClickListener {
            val intent = Intent(this, PaintingActivity::class.java)
            intent.putExtra("file", year + "_" + month + "_" + date + ".png");
            startActivityForResult(intent, 1)
        }

        mindButton.setOnClickListener {
            val intent = Intent(this, MindActivity::class.java)
            intent.putExtra("file", year + "_" + month + "_" + date + "_mind" + ".txt");
            startActivityForResult(intent, 2)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val fileName = year + "_" + month + "_" + date + ".png"
                val bitmap = BitmapFactory.decodeFile(cacheDir.path + "/" + fileName)
                paint.setImageBitmap(bitmap)
            }
            else if(requestCode == 2) {
                val fileName = year + "_" + month + "_" + date + "_mind" + ".txt"
                var mind = ""

                try {
                    val buffer = BufferedReader(FileReader(cacheDir.path + "/" + fileName))
                    while (true) {
                        val line = buffer.readLine()
                        if(line == null)
                            break
                        else
                            mind += line
                    }
                } catch(e : Exception) {
                    Log.e("error", "error: " +  e.message)
                } finally {
                    Log.i("test", "기분: " + mind)
                    if(mind == "angry")
                        mindButton.setImageResource(R.drawable.angry)
                    else if(mind == "sad")
                        mindButton.setImageResource(R.drawable.sad)
                    else if(mind == "happy")
                        mindButton.setImageResource(R.drawable.happy)
                    else if(mind == "soso")
                        mindButton.setImageResource(R.drawable.soso)
                    else if(mind == "delight")
                        mindButton.setImageResource(R.drawable.delight)
                }
            }
        }
    }
}