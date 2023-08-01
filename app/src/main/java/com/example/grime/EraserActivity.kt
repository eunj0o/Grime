package com.example.grime

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

class EraserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 폰트 적용
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_eraser)
        
        // 배경색 적용
        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        val intent = intent
        var size = intent.getFloatExtra("size", 0f)     // 지우개 크기 받아오기

        var sizeBar = findViewById<SeekBar>(R.id.size)
        var sizeText = findViewById<TextView>(R.id.sizeText)
        var saveButton = findViewById<Button>(R.id.save)

        sizeBar.progress = size.toInt()             // 현재 지우개 크기 SeekBar에 적용
        sizeText.setText(size.toInt().toString())   // 현재 크기 텍스트 출력

        sizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null) {
                    sizeText.setText(seekBar.progress.toString())       // 변경 중 텍스트에 적용
                }
                // Implement any logic you need when progress changes
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    sizeText.setText(seekBar.progress.toString())         // 변경 후 텍스트에 적용
                }
            }
        })

        saveButton.setOnClickListener {
            val intent = Intent()
            size = sizeBar.progress.toFloat()
            intent.putExtra("size", size)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}