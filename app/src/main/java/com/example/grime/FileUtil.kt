package com.example.grime

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

class FileUtil {
    companion object {
        // BufferedReader를 이용한 파일 로딩
        fun LoadFile(file: String): String? {
            var content = ""
            try {
                val buffer = BufferedReader(FileReader(file))
                while (true) {
                    val line = buffer.readLine()
                    if (line == null)
                        break
                    else
                        content += line + "\n"
                }
                return content
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
                return null
            }
        }

        // BufferedWriter를 이용한 파일 저장
        fun SaveFile(file: String, content: String) {
            try {
                val buffer = BufferedWriter(FileWriter(file, false))
                buffer.append(content)
                buffer.close()
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
            }
        }

    }
}