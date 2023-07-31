package com.example.grime

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class ColorChange : AppCompatActivity() {

    lateinit var BackButton: ImageButton
    lateinit var skyblueButton: ImageButton
    lateinit var butterButton: ImageButton
    lateinit var pinkButton: ImageButton
    lateinit var melonButton: ImageButton
    lateinit var colorchangelayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_color_change)

        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        BackButton = findViewById(R.id.colorchangeback)
        skyblueButton = findViewById(R.id.skyblue)
        butterButton = findViewById(R.id.butter)
        pinkButton = findViewById(R.id.pink)
        melonButton = findViewById(R.id.melon)


        //뒤로가기 버튼
        BackButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }

        //색 변경 버튼
         skyblueButton.setOnClickListener {
             val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
             val editor = sharedPreferences.edit()

             editor.putInt("layoutColor", Color.parseColor("#DDF0F6"))
             editor.apply()
         }

        butterButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("layoutColor", Color.parseColor("#FFFDDC"))
            editor.apply()
        }

        pinkButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("layoutColor", Color.parseColor("#FFEBF2"))
            editor.apply()
        }

        melonButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("layoutColor", Color.parseColor("#F2FFE3"))
            editor.apply()
        }


    }
}