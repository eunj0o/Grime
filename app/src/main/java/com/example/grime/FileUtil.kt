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
        fun LoadFile(file: String): String? {
            var content = ""
            try {
                val buffer = BufferedReader(FileReader(file))
                while (true) {
                    val line = buffer.readLine()
                    if (line == null)
                        break
                    else
                        content += line
                }
                return content
            } catch (e: Exception) {
                Log.e("error", "error: " + e.message)
                return null
            }
        }

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