package com.example.grime

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class CalendarRecyclerAdapter(year: Int, month: Int, diaryList: ArrayList<Int>) :
    RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(date: Int?, isDiary : Boolean)     // isDiary : 일기 작성 여부
    }

    // OnItemClickListener 참조 변수 선언
    private var itemClickListener: OnItemClickListener? = null

    // OnItemClickListener 전달 메소드
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    // {0 ~ 30/31} 까지의 날짜 리스트
    private val list: ArrayList<CalendarRecyclerItem> = ArrayList<CalendarRecyclerItem>()


    init {
        var calendar: Calendar = Calendar.getInstance()
        // 캘린더 세팅
        calendar.set(year, month - 1, 1)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        for(i in 1 until dayOfWeek) {
            this.list.add(CalendarRecyclerItem().apply { date = -1})
        }

        for(i in 1..calendar.getActualMaximum(Calendar.DATE)) {
            this.list.add(CalendarRecyclerItem().apply { date = -1})
        }

        for(diary in diaryList) {
            this.list[dayOfWeek  - 2 + diary].isDiary = true
            val fileName =  year.toString() + "_" + month + "_" + diary + "_painting" + ".png"
            val bitmap = BitmapFactory.decodeFile("내부 저장 경로" + "/" + fileName)
            this.list[dayOfWeek  - 2 + diary].image = bitmap
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            inflater.inflate(com.example.grime.R.layout.date_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calendarItem: CalendarRecyclerItem = list[position]
        if (calendarItem != null) {
            holder.dateTextView.setText(calendarItem.date)
            holder.dateImageView.setImageBitmap(calendarItem.image)
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var dateTextView: TextView
        lateinit var dateImageView: ImageView
        var isInMonth = true

        init {
            dateTextView = itemView.findViewById<TextView>(R.id.dateText)
            dateImageView = itemView.findViewById<ImageView>(R.id.dateImage)

            itemView.setOnClickListener {
                val position: Int = adapterPosition
                if (itemClickListener != null) itemClickListener!!.onItemClicked(
                    list[position].date, list[position].isDiary
                )
            }
        }
    }

}