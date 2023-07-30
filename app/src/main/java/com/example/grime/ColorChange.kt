package com.example.grime

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
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
        setContentView(R.layout.activity_color_change)

        BackButton = findViewById(R.id.colorchangeback)
        skyblueButton = findViewById(R.id.skyblue)
        butterButton = findViewById(R.id.butter)
        pinkButton = findViewById(R.id.pink)
        melonButton = findViewById(R.id.melon)
        colorchangelayout = findViewById(R.id.colorchangelayout)


        //뒤로가기 버튼
        BackButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }

        //색 변경 버튼
         skyblueButton.setOnClickListener {
             colorchangelayout.setBackgroundColor(Color.parseColor("#DDF0F6"))
         }

        butterButton.setOnClickListener {
            colorchangelayout.setBackgroundColor(Color.parseColor("#FFFDDC"))
        }

        pinkButton.setOnClickListener {
            colorchangelayout.setBackgroundColor(Color.parseColor("#FFEBF2"))
        }

        melonButton.setOnClickListener {
            colorchangelayout.setBackgroundColor(Color.parseColor("F2FFE3"))
        }


    }
}