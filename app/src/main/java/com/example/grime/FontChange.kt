package com.example.grime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

class FontChange : AppCompatActivity() {

    lateinit var BackButton: ImageButton
    lateinit var font1Button: ImageButton
    lateinit var font2Button: ImageButton
    lateinit var font3Button: ImageButton
    lateinit var writing: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_change)

        BackButton = findViewById(R.id.fontchangeback)
        font1Button = findViewById(R.id.font1Button)
        font2Button = findViewById(R.id.font2Button)
        font3Button = findViewById(R.id.font3Button)


        BackButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }
    }
}