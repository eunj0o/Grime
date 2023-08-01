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
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception

class WritingActivity : AppCompatActivity() {

    lateinit var date : String

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

        date = intent.getStringExtra("date")!!

        edtext.setText(FileUtil.LoadFile(cacheDir.path + "/" + date + "_content.txt"))

        edtButton.setOnClickListener {
            val intent = Intent()
            val file = cacheDir.path + "/" + date + "_content.txt"
            FileUtil.SaveFile(file, edtext.text.toString())
            saveStatus()
            setResult(RESULT_OK, intent)
            finish()
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