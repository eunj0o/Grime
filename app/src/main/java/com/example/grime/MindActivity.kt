package com.example.grime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.Exception

class MindActivity : AppCompatActivity() {

    lateinit var angryButton: ImageButton
    lateinit var sadButton: ImageButton
    lateinit var happyButton: ImageButton
    lateinit var sosoButton: ImageButton
    lateinit var delightButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mind)

        val fileName = intent.getStringExtra("file")

        angryButton = findViewById(R.id.angry)
        sadButton = findViewById(R.id.sad)
        happyButton = findViewById(R.id.happy)
        sosoButton = findViewById(R.id.soso)
        delightButton = findViewById(R.id.delight)

        angryButton.setOnClickListener {
            val intent = Intent()
            try {
                val buffer = BufferedWriter(FileWriter(cacheDir.path + "/" + fileName))
                buffer.append("angry")
                buffer.close()
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        sadButton.setOnClickListener {
            val intent = Intent()
            try {
                val buffer = BufferedWriter(FileWriter(cacheDir.path + "/" + fileName))
                buffer.append("sad")
                buffer.close()
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        happyButton.setOnClickListener {
            val intent = Intent()
            try {
                val buffer = BufferedWriter(FileWriter(cacheDir.path + "/" + fileName))
                buffer.append("happy")
                buffer.close()
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        sosoButton.setOnClickListener {
            val intent = Intent()
            try {
                val buffer = BufferedWriter(FileWriter(cacheDir.path + "/" + fileName))
                buffer.append("soso")
                buffer.close()
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        delightButton.setOnClickListener {
            val intent = Intent()
            try {
                val buffer = BufferedWriter(FileWriter(cacheDir.path + "/" + fileName))
                buffer.append("delight")
                buffer.close()
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}