package com.example.grime

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton

class MainSetting : AppCompatActivity() {

    lateinit var FontChangeButton: ImageButton
    lateinit var ColorChangeButton: ImageButton
    lateinit var TrashCanButton: ImageButton
    lateinit var SettingBackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 폰트 적용
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        
        setContentView(R.layout.activity_main_setting)

        // 배경색 적용
        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        FontChangeButton = findViewById(R.id.fontChange)
        ColorChangeButton = findViewById(R.id.colorChange)
        TrashCanButton = findViewById(R.id.trashcan)
        SettingBackButton = findViewById(R.id.mainsettingback)


        FontChangeButton.setOnClickListener {
            var intent = Intent(this, FontChangeActivity::class.java)
            startActivity(intent)
        }

        ColorChangeButton.setOnClickListener {
            var intent = Intent(this, ColorChangeActivity::class.java)
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