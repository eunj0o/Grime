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
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_eraser)

        val intent = intent
        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)
        var size = intent.getFloatExtra("size", 0f)

        var sizeBar = findViewById<SeekBar>(R.id.size)
        var sizeText = findViewById<TextView>(R.id.sizeText)
        var saveButton = findViewById<Button>(R.id.save)

        sizeBar.progress = size.toInt()
        sizeText.setText(size.toInt().toString())

        sizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null) {
                    sizeText.setText(seekBar.progress.toString())
                }
                // Implement any logic you need when progress changes
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    sizeText.setText(seekBar.progress.toString())
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