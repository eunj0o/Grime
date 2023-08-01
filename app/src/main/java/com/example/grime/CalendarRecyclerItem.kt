package com.example.grime

import android.graphics.Bitmap
import android.view.View

class CalendarRecyclerItem {
    var date : Int = 1
    var paintImage : Bitmap? = null
    var visibility : Int = View.GONE
    var tint : Int = 0
}