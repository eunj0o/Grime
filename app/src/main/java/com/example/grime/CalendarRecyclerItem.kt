package com.example.grime

import android.graphics.Bitmap
import android.view.View

class CalendarRecyclerItem {
    var date : Int = 1                  // 날짜
    var paintImage : Bitmap? = null     // 달력 날짜에 표시되는 그림 일기 그림
    var visibility : Int = View.GONE    // ! 표시 표시 여부
    var tint : Int = 0                  // ! 표시 색깔
}