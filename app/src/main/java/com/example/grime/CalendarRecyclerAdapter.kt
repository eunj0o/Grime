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

        // 1일과 같은 행의 이전 요일들 date = -1로 세팅 (달력에서 안보이게 하기 위해서)
        for (i in 1 until dayOfWeek) {
            this.list.add(CalendarRecyclerItem().apply { date = -1 })
        }

        // 1일부터 해당 달의 끝날까지 날짜 설정
        for (i in 1..calendar.getActualMaximum(Calendar.DATE)) {
            this.list.add(CalendarRecyclerItem().apply { date = i })
        }

        // 일기장 상태가 저장된 json 불러오기
        val json = getStatusJSON(filesDir)

        for(key in json.keys()) {
            // 해당 키에서 연도와 달이 맞는 키만 추출
            val isValid = key.contains(year.toString() + "_" + month.toString())
            if(isValid) {

                val diaryDay = key.substring((year.toString() + "_" + month.toString() + "_").length).toInt()   // 일기가 저장된 날
                val status = checkStatus(json, key)     // 일기장 상태(ex: 작성중, 작성 완료, 수정중 등)

                if(status == "temp") {
                    // 저장하지 않은 작성 상태
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date        // 기존 날짜
                    
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.VISIBLE                            // ! 보이게
                        tint = Color.parseColor("#ff7f00")         // ! 주황색으로 표시
                        paintImage = selectImage(key, cacheDir, filesDir)   // 그림 일기 이미지 표시
                    })
                } else if(status == "editing") {
                    // 저장했지만 작성 중인 상태
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date        // 기존 날짜
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.VISIBLE                           // ! 보이게
                        tint = Color.parseColor("#4b89dc")         // ! 파란색으로 표시
                        paintImage = selectImage(key, cacheDir, filesDir)   // 그림 일기 이미지 표시
                    })
                    Log.i("test", this.list[dayOfWeek - 2 + diaryDay].paintImage.toString())
                } else if(status == "completed") {
                    // 저장한 상태
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date        // 기존 날짜
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.GONE                              // ! 보이지 않게
                        tint = Color.parseColor("#ffffff")         // ! 색깔
                        paintImage = selectImage(key, cacheDir, filesDir)   // 그림 일기 이미지 표시
                    })
                } else {
                    // 그 외 다른 상태
                    val date1 = this.list[dayOfWeek - 2 + diaryDay].date        // 기존 날짜
                    this.list.set(dayOfWeek - 2 + diaryDay, CalendarRecyclerItem().apply {
                        date = date1
                        visibility = View.GONE                                // ! 보이지 않게
                        tint = Color.parseColor("#ffffff")          // ! 색깔
                        paintImage = selectImage(key, cacheDir, filesDir)   // 그림 일기 이미지 표시
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

    // 캐시 이미지 우선 로딩하고, 실패하면 내부 저장소 이미지 로딩
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
        
        // 해당 달의 1일과 같은 행이면서 이전 요일이 아닐 경우에만
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
        lateinit var datePaintImage : ImageView
        lateinit var dateStatusImage : ImageView

        init {
            dateTextView = itemView.findViewById<TextView>(R.id.dateText)
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