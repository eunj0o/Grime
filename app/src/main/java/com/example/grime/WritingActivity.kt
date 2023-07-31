package com.example.grime

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception

class WritingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_writing)

        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        var edtButton = findViewById<Button>(R.id.edtButton)
        var edtext = findViewById<EditText>(R.id.edtext)

        val fileName = intent.getStringExtra("file")

        edtButton.setOnClickListener {
            val intent = Intent()
            try {
                val buffer = BufferedWriter(FileWriter(cacheDir.path + "/" + fileName, false))
                buffer.append(edtext.text)
                buffer.close()
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
            } finally {
                setResult(RESULT_OK, intent)
                finish()
            }

        }


    }
}