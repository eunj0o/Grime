package com.example.grime

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import java.util.Calendar

class CalendarRecyclerAdapter(year: Int, month: Int, cacheDir : String, filesDir : String) :
    RecyclerView.Adapter<CalendarRecyclerAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(year : Int, month : Int, date: Int?)     // isDiary : 일기 작성 여부
    }

    // OnItemClickListener 참조 변수 선언
    private var itemClickListener: OnItemClickListener? = null

    // OnItemClickListener 전달 메소드
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    // {0 ~ 30/31} 까지의 날짜 리스트
    private val list: ArrayList<CalendarRecyclerItem> = ArrayList<CalendarRecyclerItem>()
    private val year = year
    private val month = month


    init {
        updateItem(cacheDir, filesDir)
    }

    fun updateItem(cacheDir: String, filesDir: String) {
        var calendar: Calendar = Calendar.getInstance()
        // 캘린더 세팅
        calendar.set(year, month - 1, 1)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        for (i in 1 until dayOfWeek) {
            this.list.add(CalendarRecyclerItem().apply { date = -1 })
        }

        for (i in 1..calendar.getActualMaximum(Calendar.DATE)) {
            this.list.add(CalendarRecyclerItem().apply { date = i })
        }

        val json = getStatusJSON(filesDir)

        for(key in json.keys()) {

            val isValid = key.contains(year.toString() + "_" + month.toString())
            if(isValid) {
                Log.i("info", "valid")
                val diaryDay = key.substring((year.toString() + "_" + month.toString() + "_").length).toInt()
                val status = checkStatus(json, key)
                //     Log.i("info", "list: " + key)
                if(status == "temp") {
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.VISIBLE
                        tint = Color.parseColor("#ff7f00")
                        paintImage = selectImage(key, cacheDir, filesDir)
                    })
                } else if(status == "editing") {
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.VISIBLE
                        tint = Color.parseColor("#4b89dc")
                        paintImage = selectImage(key, cacheDir, filesDir)
                    })
                    Log.i("test", this.list[dayOfWeek - 2 + diaryDay].paintImage.toString())
                } else if(status == "completed") {
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.GONE
                        tint = Color.parseColor("#ffffff")
                        paintImage = selectImage(key, cacheDir, filesDir)
                    })
                } else {
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.GONE
                        tint = Color.parseColor("#ffffff")
                        paintImage = selectImage(key, cacheDir, filesDir)
                    })
                }
            }
        }
        notifyDataSetChanged()
    }

    fun getStatusJSON(filesDir : String) : JSONObject {
        val file = filesDir + "/" + "status.json"
        val loadedData = FileUtil.LoadFile(file)
        var json: JSONObject
        if (loadedData != null)
            json = JSONObject(loadedData)
        else
            json = JSONObject()
        return json
    }

    fun checkStatus(json : JSONObject, key : String) : String? {
        try {
            val status = json.getString(key)
            return status
        } catch (e: Exception) {
            return null
        }
    }

    fun selectImage(key : String, cacheDir : String, filesDir: String) : Bitmap? {
        val fileName = key + ".png"
        try {
            val bitmap = BitmapFactory.decodeFile("$cacheDir/$fileName")
            return bitmap
        } catch (e : Exception) {
            try {
                val bitmap = BitmapFactory.decodeFile("$filesDir/$fileName")
                return bitmap
            } catch (e : Exception) {
                Log.e("error", "error: " + e.message)
                return null
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            inflater.inflate(R.layout.date_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calendarItem: CalendarRecyclerItem = list[position]
        if (calendarItem != null && calendarItem.date != -1) {
                holder.dateTextView.setText(calendarItem.date.toString())
            if(calendarItem.paintImage != null)
                holder.datePaintImage.setImageBitmap(calendarItem.paintImage)
            holder.dateStatusImage.visibility = calendarItem.visibility
            holder.dateStatusImage.imageTintList = ColorStateList.valueOf(calendarItem.tint)
        }

    }


    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var dateTextView: TextView
    //    lateinit var dateImageView: ImageView
        lateinit var datePaintImage : ImageView
        lateinit var dateStatusImage : ImageView

        init {
            dateTextView = itemView.findViewById<TextView>(R.id.dateText)
     //       dateImageView = itemView.findViewById<ImageView>(R.id.dateImage)
            datePaintImage = itemView.findViewById<ImageView>(R.id.datePaintImage)
            dateStatusImage = itemView.findViewById<ImageView>(R.id.dateCalendarStatus)

            itemView.setOnClickListener {
                val position: Int = adapterPosition
                if (itemClickListener != null && list[position].date != -1) itemClickListener!!.onItemClicked(year, month,
                    list[position].date
                )
            }
        }
    }

}