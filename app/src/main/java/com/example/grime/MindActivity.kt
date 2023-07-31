package com.example.grime

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import org.json.JSONObject
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

        val date = intent.getStringExtra("date")

        angryButton = findViewById(R.id.angry)
        sadButton = findViewById(R.id.sad)
        happyButton = findViewById(R.id.happy)
        sosoButton = findViewById(R.id.soso)
        delightButton = findViewById(R.id.delight)

        angryButton.setOnClickListener {
            val intent = Intent()
            val file = cacheDir.path + "/" + "mind.json"
            val loadedData = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedData != null)
                json = JSONObject(loadedData)
            else
                json = JSONObject()
            json.put(date, "angry")
            FileUtil.SaveFile(file, json.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
        sadButton.setOnClickListener {
            val intent = Intent()
            val file = cacheDir.path + "/" + "mind.json"
            val loadedData = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedData != null)
                json = JSONObject(loadedData)
            else
                json = JSONObject()
            json.put(date, "sad")
            FileUtil.SaveFile(file, json.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
        happyButton.setOnClickListener {
            val intent = Intent()
            val file = cacheDir.path + "/" + "mind.json"
            val loadedData = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedData != null)
                json = JSONObject(loadedData)
            else
                json = JSONObject()
            json.put(date, "happy")
            FileUtil.SaveFile(file, json.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
        sosoButton.setOnClickListener {
            val intent = Intent()
            val file = cacheDir.path + "/" + "mind.json"
            val loadedData = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedData != null)
                json = JSONObject(loadedData)
            else
                json = JSONObject()
            json.put(date, "soso")
            FileUtil.SaveFile(file, json.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
        delightButton.setOnClickListener {
            val intent = Intent()
            val file = cacheDir.path + "/" + "mind.json"
            val loadedData = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedData != null)
                json = JSONObject(loadedData)
            else
                json = JSONObject()
            json.put(date, "delight")
            FileUtil.SaveFile(file, json.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}