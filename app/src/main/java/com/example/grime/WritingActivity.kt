package com.example.grime

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class WritingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        var edtButton = findViewById<Button>(R.id.edtButton)
        var edtext = findViewById<EditText>(R.id.edtext)

        edtButton.setOnClickListener {
            val intent = Intent(this, PaintingDiaryActivity::class.java)
            intent.putExtra("text", edtext.getText().toString())
            startActivityForResult(intent, 1)
        }
    }
}