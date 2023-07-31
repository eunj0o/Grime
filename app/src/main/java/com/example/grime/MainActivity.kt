package com.example.grime

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    lateinit var menuButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme, )
        setContentView(R.layout.activity_main)

        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        var calander = findViewById<CalendarView>(R.id.calendarView)

        menuButton = findViewById(R.id.mainmenu)
        menuButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }

        calander.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val intent = Intent(this, PaintingDiaryActivity::class.java)
            intent.putExtra("year", i)
            intent.putExtra("month", i2)
            intent.putExtra("date", i3)
            startActivity(intent)
        }


    }
}