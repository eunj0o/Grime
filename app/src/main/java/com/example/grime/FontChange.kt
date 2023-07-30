package com.example.grime

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat

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


        //뒤로가기 버튼
        BackButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }


        val titleText = PaintingDiaryActivity().titleText
        val writeText = PaintingDiaryActivity().writeText



        //폰트 가져오기
        val nanumjunghaksaeng: Typeface? = ResourcesCompat.getFont(this, R.font.nanumjunghaksaeng)
        val nanumneurisneuris: Typeface? = ResourcesCompat.getFont(this, R.font.nanumneurisneuris)
        val nanumjarhagoisseo: Typeface? = ResourcesCompat.getFont(this, R.font.nanumjarhagoisseo)

        //폰트 변경 버튼 누르면 폰트 변경
        font1Button.setOnClickListener {
            titleText.typeface = nanumjunghaksaeng
            writeText.typeface = nanumjunghaksaeng
        }

        font2Button.setOnClickListener {
            titleText.typeface = nanumneurisneuris
            writeText.typeface = nanumneurisneuris
        }

        font3Button.setOnClickListener {
            titleText.typeface = nanumjarhagoisseo
            writeText.typeface = nanumjarhagoisseo
        }

    }
}