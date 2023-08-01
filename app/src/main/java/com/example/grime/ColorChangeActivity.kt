package com.example.grime

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout

class ColorChangeActivity : AppCompatActivity() {

    lateinit var backButton: ImageButton
    lateinit var skyblueButton: ImageButton
    lateinit var butterButton: ImageButton
    lateinit var pinkButton: ImageButton
    lateinit var melonButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        
        // 폰트 적용
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_color_change)
        
        // 배경색 적용
        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        backButton = findViewById(R.id.colorchangeback)
        skyblueButton = findViewById(R.id.skyblue)
        butterButton = findViewById(R.id.butter)
        pinkButton = findViewById(R.id.pink)
        melonButton = findViewById(R.id.melon)


        //뒤로가기 버튼
        backButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }

        // 하늘색으로 배경색 지정
         skyblueButton.setOnClickListener {
             val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
             val editor = sharedPreferences.edit()

             editor.putInt("layoutColor", Color.parseColor("#DDF0F6"))
             editor.apply()
         }

        // 버터색으로 배경색 지정
        butterButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("layoutColor", Color.parseColor("#FFFDDC"))
            editor.apply()
        }

        // 핑크색으로 배경색 지정
        pinkButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("layoutColor", Color.parseColor("#FFEBF2"))
            editor.apply()
        }

        // 멜론색으로 배경색 지정
        melonButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("layoutColor", Color.parseColor("#F2FFE3"))
            editor.apply()
        }


    }
}