package com.example.grime

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat

class FontChange : AppCompatActivity() {

    lateinit var BackButton: ImageButton
    lateinit var font1Button: ImageButton
    lateinit var font2Button: ImageButton
    lateinit var font3Button: ImageButton
    lateinit var writing: EditText

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_font_change)

        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        BackButton = findViewById(R.id.fontchangeback)
        font1Button = findViewById(R.id.font1Button)
        font2Button = findViewById(R.id.font2Button)
        font3Button = findViewById(R.id.font3Button)


        //뒤로가기 버튼
        BackButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }

        //폰트 변경 버튼 누르면 폰트 변경
        font1Button.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("font", R.style.nanumjunghaksaeng)
            editor.apply()
        }

        font2Button.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("font", R.style.nanumneurisneuris)
            editor.apply()
        }

        font3Button.setOnClickListener {
            val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("font", R.style.nanumjarhagoisseo)
            editor.apply()
        }

    }
}