package com.example.grime

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class WritingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)

        var edtButton = findViewById<Button>(R.id.edtButton)
        var edtext = findViewById<EditText>(R.id.edtext)

        val intent = intent
        val fileName = intent.getStringExtra("file")

        edtButton.setOnClickListener {
            val storage = cacheDir

            val tempFile = File(storage, fileName)

            try {

                // 자동으로 빈 파일을 생성합니다.
                tempFile.createNewFile()

                // 파일을 쓸 수 있는 스트림을 준비합니다.
                val out = FileOutputStream(tempFile)

                out.write(edtext.getText().toString().toByteArray())

                // 스트림 사용후 닫아줍니다.
                out.close()
                Log.i("test", fileName + " success save")
            } catch (e: FileNotFoundException) {
                Log.e("MyTag", "FileNotFoundException : " + e.message)
            } catch (e: IOException) {
                Log.e("MyTag", "IOException : " + e.message)
            } finally {
                setResult(RESULT_OK, intent)
                finish()
            }

        }
    }
}