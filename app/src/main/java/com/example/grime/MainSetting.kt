package com.example.grime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainSetting : AppCompatActivity() {

    lateinit var FontChangeButton: ImageButton
    lateinit var ColorChangeButton: ImageButton
    lateinit var TrashCanButton: ImageButton
    lateinit var SettingBackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_setting)

        FontChangeButton = findViewById(R.id.fontChange)
        ColorChangeButton = findViewById(R.id.colorChange)
        TrashCanButton = findViewById(R.id.trashcan)


        FontChangeButton.setOnClickListener {
            var intent = Intent(this, FontChange::class.java)
            startActivity(intent)
        }

        ColorChangeButton.setOnClickListener {
            var intent = Intent(this, ColorChange::class.java)
            startActivity(intent)
        }

        TrashCanButton.setOnClickListener {
            var intent = Intent(this, TrashCan::class.java)
            startActivity(intent)
        }

        SettingBackButton.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}