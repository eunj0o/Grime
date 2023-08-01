package com.example.grime

import android.graphics.Bitmap

class CalendarRecyclerItem {
    var date : Int = 1
    var isDiary : Boolean = false       // 일기 작성 여부
    var image : Bitmap? = null
}