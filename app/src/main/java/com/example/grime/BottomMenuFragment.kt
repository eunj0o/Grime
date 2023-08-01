package com.example.grime

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class bottommenu : BottomSheetDialogFragment() {

    lateinit var saveButton : Button
    lateinit var shareButton : Button
    lateinit var dataPassListener : OnDataPassListener      // 상속받는 액티비티로 데이터 전달
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_bottommenu, container, false)
        
        // 폰트 적용
        val sharedPreferences = activity?.getSharedPreferences("theme", Context.MODE_PRIVATE)
        ThemeUtil.applyTheme(sharedPreferences!!, activity?.theme!!)

        // 배경색 적용
        var mainLayout = view.findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)
        
        saveButton = view.findViewById<Button>(R.id.savebutton)
        shareButton = view.findViewById<Button>(R.id.sharebutton)

        saveButton.setOnClickListener {
            dataPassListener.onDataPass("imageSave")
            val fragmentManager = activity?.getSupportFragmentManager();
            fragmentManager?.beginTransaction()?.remove(this)?.commit();
            fragmentManager?.popBackStack();
        }

        shareButton.setOnClickListener {
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