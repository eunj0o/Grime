package com.example.grime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    lateinit var MainMenuButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainMenuButton = findViewById(R.id.mainmenu)

        MainMenuButton.setOnClickListener {
            val intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }
    }
}