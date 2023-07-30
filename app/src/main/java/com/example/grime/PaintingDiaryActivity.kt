package com.example.grime

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.EditText
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
    lateinit var titleEdit : EditText
    lateinit var paintingDiaryBackButton: ImageButton
    lateinit var mindButton : ImageButton
    lateinit var writeTextView : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_diary)

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


        diaryDate.setText(year + "년 " + month + "월 " + date + "일")


        paint.setOnClickListener {
            val intent = Intent(this, PaintingActivity::class.java)
            intent.putExtra("file", year + "_" + month + "_" + date + ".png");
            startActivityForResult(intent, 1)
        }

        paintingDiaryBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        mindButton.setOnClickListener {
            val intent = Intent(this, MindActivity::class.java)
            intent.putExtra("file", year + "_" + month + "_" + date + "_mind" + ".txt");
            startActivityForResult(intent, 2)
        }

        writeTextView.setOnClickListener {
            val intent = Intent(this, WritingActivity::class.java)
            intent.putExtra("file", year + "_" + month + "_" + date + "_content" + ".txt");
            startActivityForResult(intent, 3)
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
            else if(requestCode == 3) {
                val fileName = year + "_" + month + "_" + date + "_content" + ".txt"
                var content = ""

                try {
                    val buffer = BufferedReader(FileReader(cacheDir.path + "/" + fileName))
                    while (true) {
                        val line = buffer.readLine()
                        if(line == null)
                            break
                        else
                            content += line
                    }
                } catch(e : Exception) {
                    Log.e("error", "error: " +  e.message)
                } finally {
                    writeTextView.setText(content.toString())
                }
            }
        }
    }
}