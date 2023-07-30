package com.example.grime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class Painting : AppCompatActivity() {

    lateinit var paintingbackButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_painting)

        paintingbackButton = findViewById(R.id.paintbackButton)

        paintingbackButton.setOnClickListener {
            var intent = Intent(this, PaintingDiaryActivity::class.java)
            startActivity(intent)
        }
    }
}