package com.example.grime

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class ColorRecyclerAdapter(list: ArrayList<ColorRecyclerItem>) :
    RecyclerView.Adapter<ColorRecyclerAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClicked(position: Int, color: Int?)
    }

    // OnItemClickListener 참조 변수 선언
    private var itemClickListener: OnItemClickListener? = null

    // OnItemClickListener 전달 메소드
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    private var checkList : ArrayList<Boolean> = ArrayList<Boolean>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var colorItem: View
        lateinit var checkItem : ImageView

        init {
            colorItem = itemView.findViewById<View>(R.id.colorItem)
            checkItem = itemView.findViewById<ImageView>(R.id.checkItem)

            itemView.setOnClickListener {

                var color = 0
                val position: Int = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 색깔 지정하면 체크 표시하고, 나머지 아이템은 체크 표시 해제
                    val item: ColorRecyclerItem = list[position]
                    color = item.color
                    for(i in 0..checkList.size - 1)
                        checkList.set(i, false)
                    checkList.set(position, true)
                    notifyDataSetChanged()
                    itemClickListener!!.onItemClicked(position, color)
                }

            }
        }
    }

    private var list: ArrayList<ColorRecyclerItem>

    init {
        this.list = list
        for(i in 1..list.size)
            this.checkList.add(false)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View =
            inflater.inflate(R.layout.activity_color_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: ColorRecyclerItem = list[position]
        holder.colorItem.setBackgroundColor(item.color)
        if(checkList[position])
            holder.checkItem.visibility = View.VISIBLE          // 체크가 true이면 체크 표시 on
        else
            holder.checkItem.visibility = View.GONE             // 체크가 false이면 체크 표시 off
    }

    override fun getItemCount(): Int {
        return list.size
    }
}