package com.example.grime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class PaintingDiary : AppCompatActivity() {

    lateinit var titleText : EditText
    lateinit var writeText : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting_diary)

        titleText = findViewById(R.id.titleEdit)
        writeText = findViewById(R.id.writeEdit)
    }
}