package com.example.grime

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class bottommenu : BottomSheetDialogFragment() {

    lateinit var savebutton : Button
    lateinit var sharebutton : Button
    lateinit var dataPassListener : OnDataPassListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_bottommenu, container, false)
        savebutton = view.findViewById<Button>(R.id.savebutton)
        sharebutton = view.findViewById<Button>(R.id.sharebutton)

        savebutton.setOnClickListener {
            dataPassListener.onDataPass("imageSave")
            val fragmentManager = activity?.getSupportFragmentManager();
            fragmentManager?.beginTransaction()?.remove(this)?.commit();
            fragmentManager?.popBackStack();
        }

        sharebutton.setOnClickListener {
            dataPassListener.onDataPass("shareImage")
            val fragmentManager = activity?.getSupportFragmentManager();
            fragmentManager?.beginTransaction()?.remove(this)?.commit();
            fragmentManager?.popBackStack();
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassListener = context as OnDataPassListener
    }

    interface OnDataPassListener {
        fun onDataPass(data : String?)
    }

}