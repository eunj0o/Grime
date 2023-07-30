package com.example.grime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class TrashCan : AppCompatActivity() {

    lateinit var BackButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trash_can)

        BackButton = findViewById(R.id.trashcanback)


        BackButton.setOnClickListener {
            var intent = Intent(this, MainSetting::class.java)
            startActivity(intent)
        }
    }
}