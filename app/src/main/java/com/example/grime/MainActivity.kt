package com.example.grime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    lateinit var menuButton: ImageButton
    lateinit var calendarRecycler : RecyclerView
    lateinit var weekRecycler : RecyclerView


    lateinit var calendarAdapter : CalendarRecyclerAdapter
    lateinit var weekAdapter : WeekAdapter

    lateinit var calendar : Calendar
    lateinit var yearCalendar : TextView
    lateinit var monthCalendar : TextView
    lateinit var previousButton : ImageButton
    lateinit var nextButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 폰트 적용
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_main)

        // 배경색 적용
        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)

        // 현재 날짜 적용한 캘린더 Get
        calendar = getCalendar(savedInstanceState)!!
        
        calendarRecycler = findViewById<RecyclerView>(R.id.dayList)
        weekRecycler = findViewById<RecyclerView>(R.id.weekList)

        // 리사이클러뷰 layoutManager grid로 적용, 캐시 사이즈 적용
        val gridLayoutManager = GridLayoutManager(this, 7)
        calendarRecycler.layoutManager = gridLayoutManager
        calendarRecycler.setItemViewCacheSize(42)

        // 리사이클러뷰 layoutManager grid로 적용, 캐시 사이즈 적용
        val gridLayoutManager_week = GridLayoutManager(this, 7)
        weekRecycler.layoutManager = gridLayoutManager_week
        weekRecycler.setItemViewCacheSize(7)


        setWeeklistbar()        // 요일을 나타내는 리스트 set
        initCalendarAdapter()   // calendarAdapter init


        yearCalendar = findViewById(R.id.yearCalendar)
        monthCalendar = findViewById(R.id.monthCalendar)
        previousButton = findViewById(R.id.previous)
        nextButton = findViewById(R.id.next)

        // 년과 달 default 적용
        yearCalendar.setText((calendar.get(Calendar.YEAR)).toString())
        monthCalendar.setText((calendar.get(Calendar.MONTH) + 1).toString())


        menuButton = findViewById(R.id.mainmenu)
        menuButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }

        previousButton.setOnClickListener {
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DATE)
            
            // 해당 달이 1월인지에 따라 다르게 적용
            if(month + 1 > 1) {
                calendar.set(year, month - 1, day)

            }
            else {
                calendar.set(year - 1, 12, day)
            }
            yearCalendar.setText((calendar.get(Calendar.YEAR)).toString())
            monthCalendar.setText((calendar.get(Calendar.MONTH) + 1).toString())
            initCalendarAdapter()
        }

        nextButton.setOnClickListener {
            var year = calendar.get(Calendar.YEAR)
            var month = calendar.get(Calendar.MONTH)
            var day = calendar.get(Calendar.DATE)

            // 해당 달이 12월인지에 따라 다르게 적용
            if(month + 1 < 12) {
                calendar.set(year, month + 1, day)
            }
            else {
                calendar.set(year + 1, 1, day)
            }
            yearCalendar.setText((calendar.get(Calendar.YEAR)).toString())
            monthCalendar.setText((calendar.get(Calendar.MONTH) + 1).toString())
            initCalendarAdapter()
        }

    }
    override fun onResume() {
        super.onResume()
        calendar[Calendar.DAY_OF_MONTH] = 1
    }

    private fun getCalendar(savedInstanceState: Bundle?): Calendar? {
        calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1

        calendar.set(year, month - 1, 1)

        return calendar
    }

    private fun initCalendarAdapter() {
        val intent = Intent(this, PaintingDiaryActivity::class.java)
        calendarAdapter = CalendarRecyclerAdapter(
            calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1, cacheDir.path, filesDir.path
        )
        calendarAdapter.setOnItemClickListener(object : CalendarRecyclerAdapter.OnItemClickListener {
            override fun onItemClicked(year: Int, month: Int, date: Int?) {

                intent.putExtra("year", year)
                intent.putExtra("month", month)
                intent.putExtra("date", date)
                startActivity(intent)
            }
        })
        calendarRecycler.adapter = calendarAdapter
    }

    private fun setWeeklistbar() {
        val dayOfWeekList = ArrayList<String>()
        dayOfWeekList.add("SUN")
        dayOfWeekList.add("MON")
        dayOfWeekList.add("TUE")
        dayOfWeekList.add("WED")
        dayOfWeekList.add("THU")
        dayOfWeekList.add("FRI")
        dayOfWeekList.add("SAT")
        weekAdapter = WeekAdapter(this, dayOfWeekList)
        weekRecycler.setAdapter(weekAdapter)
    }
}