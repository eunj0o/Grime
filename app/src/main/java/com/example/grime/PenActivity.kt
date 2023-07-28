package com.example.grime

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class PenActivity : AppCompatActivity() {

    lateinit var colorRecycler: RecyclerView
    lateinit var colorList: ArrayList<ColorRecyclerItem>
    lateinit var colorRecyclerAdapter: ColorRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pen)



        var widthBar = findViewById<SeekBar>(R.id.width)
        var widthText = findViewById<TextView>(R.id.widthText)
        var transparencyBar = findViewById<SeekBar>(R.id.transparency)
        var transparencyText = findViewById<TextView>(R.id.transparencyText)
        var saveButton = findViewById<Button>(R.id.save)

        val intent = intent
        var color = intent.getIntExtra("color", Color.parseColor("#00ffffff"))
        var width = intent.getFloatExtra("width", 0f)
        widthBar.progress = width.toInt()
        widthText.setText(width.toInt().toString())
        var strColor = convertColorToStr(color)
        var transparency = getAlpha(strColor)
        var rgb = getRGB(strColor)

        colorRecycler = findViewById<RecyclerView>(R.id.color)
        colorList = ArrayList<ColorRecyclerItem>()

        setColorList()
        colorRecyclerAdapter = ColorRecyclerAdapter(colorList)

        colorRecyclerAdapter.setOnItemClickListener(object : ColorRecyclerAdapter.OnItemClickListener {
            override fun onItemClicked(position: Int, color: Int?) {
                if (color != null) {
                    rgb = color
                    Log.i("test", rgb.toString())
                    Log.i("test", String.format("%08X", rgb))
                }
            }
        })
        colorRecycler.setAdapter(colorRecyclerAdapter)

        transparencyBar.progress = (transparency.toFloat() / 255 * 100).toInt()
        transparencyText.setText( (transparency.toFloat() / 255 * 100).toInt().toString())

        widthBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null) {
                    widthText.setText(seekBar.progress.toString())
                }
                // Implement any logic you need when progress changes
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    widthText.setText(seekBar.progress.toString())
                }
            }
        })

        transparencyBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (seekBar != null) {
                    transparencyText.setText(seekBar.progress.toString())
                }
                // Implement any logic you need when progress changes
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    transparencyText.setText(seekBar.progress.toString())
                }
            }
        })

        saveButton.setOnClickListener {
            val intent = Intent()
            width = widthBar.progress.toFloat()
            transparency = (transparencyBar.progress.toFloat() / 100 * 255).toInt()
            intent.putExtra("color", getColor(transparency, rgb))
            intent.putExtra("width", width)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    fun setColorList() {
        val sampleColors : ArrayList<Int> = ArrayList<Int>()
        sampleColors.add(Color.BLACK)
        sampleColors.add(Color.RED)
        sampleColors.add(Color.parseColor("#ff7f00"))       //주황색
        sampleColors.add(Color.YELLOW)
        sampleColors.add(Color.GREEN)
        sampleColors.add(Color.BLUE)
        sampleColors.add(Color.parseColor("#8b00ff"))       //보라색

        for(sampleColor in sampleColors) {
            var item: ColorRecyclerItem = ColorRecyclerItem()
            item.color = sampleColor
            colorList.add(item);
        }
    }

    fun convertColorToStr(color : Int) : String {
        return String.format("%08X", color)
    }

    fun getAlpha(color : String) : Int {
        return color.substring(0 until 2).toInt(16)
    }

    fun getRGB(color: String) : Int {
        return color.substring(2).toInt(16)
    }

    fun getColor(alpha : Int, rgb : Int) : Int {

        val strColor = String.format("#%02X", alpha).plus(String.format("%08X", rgb).substring(2))
        Log.i("test", strColor)
        return Color.parseColor(strColor)
    }
}