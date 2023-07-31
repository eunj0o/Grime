package com.example.grime

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class ThemeUtil {
    companion object {
        fun applyTheme(sharedPreferences: SharedPreferences, theme: Resources.Theme) {
            if (sharedPreferences.contains("font")) {
                val fontId = sharedPreferences.getInt("font", 0)

                theme.applyStyle(fontId, true)
            }
        }

        fun <T : ViewGroup>applyViewStyle(sharedPreferences: SharedPreferences, layout: T) {
            if (sharedPreferences.contains("layoutColor")) {
                val layoutColor = sharedPreferences.getInt("layoutColor", 0)

                layout.setBackgroundColor(layoutColor)
            } else {
                layout.setBackgroundColor(Color.parseColor("#DDF0F6"))
            }
        }
    }
}