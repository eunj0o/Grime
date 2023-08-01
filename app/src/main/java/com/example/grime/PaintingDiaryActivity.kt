package com.example.grime

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path


class PaintingDiaryActivity : AppCompatActivity(), bottommenu.OnDataPassListener {

    lateinit var year : String
    lateinit var month : String
    lateinit var date : String
    lateinit var paint : ImageView
    lateinit var titleEdit : EditText
    lateinit var paintingDiaryBackButton: ImageButton
    lateinit var mindButton : ImageButton
    lateinit var writeTextView : TextView
    lateinit var saveButton : Button
    lateinit var editButton : Button
    lateinit var deleteButton : Button
    var status : String = "new"
    var editStatus : Boolean = true
    lateinit var loadStatus : ArrayList<Boolean>

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("theme", Context.MODE_PRIVATE)

        ThemeUtil.applyTheme(sharedPreferences, theme)
        setContentView(R.layout.activity_painting_diary)


        var mainLayout = findViewById<ViewGroup>(R.id.mainLayout)
        ThemeUtil.applyViewStyle(sharedPreferences, mainLayout)


        //메뉴 버튼 누르면 아래에서 나옴
        val menubutton = findViewById<ImageButton>(R.id.menuButton)
        menubutton.setOnClickListener {
            val bottommenu = bottommenu()

            bottommenu.show(supportFragmentManager, bottommenu.tag)
        }


        val intent = intent
        var diaryDate = findViewById<TextView>(R.id.diaryDate)
        paint = findViewById<ImageView>(R.id.paint)
        writeTextView = findViewById<TextView>(R.id.writeTextView)
        mindButton = findViewById(R.id.mindButton)
        saveButton = findViewById<Button>(R.id.saveButton)
        editButton = findViewById<Button>(R.id.editButton)
        deleteButton = findViewById<Button>(R.id.deleteButton)

        year = intent.getIntExtra("year", 0).toString()
        month = intent.getIntExtra("month", 0).toString()
        date = intent.getIntExtra("date", 0).toString()

        titleEdit = findViewById(R.id.titleEdit)
        paintingDiaryBackButton = findViewById(R.id.paintingdiarybackButton)
        loadStatus = ArrayList<Boolean>()
        for(i in 1..4)
            loadStatus.add(false)
        loadCache()
        load()

        Log.i("i", "status: " + status)
        editStatus = status == "editing" || status == "temp" || status == "new"
        if(!editStatus) {
            saveButton.visibility = View.GONE
            editButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            titleEdit.isEnabled = false
        }


        diaryDate.setText(year + "년 " + month + "월 " + date + "일")

        titleEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(title: Editable?) {
               saveCacheTitle(title.toString())
                saveTempStatus()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        paint.setOnClickListener {
            if(editStatus == true) {
                val intent = Intent(this, PaintingActivity::class.java)
                intent.putExtra("date", year + "_" + month + "_" + date);
                startActivityForResult(intent, 1)
            }
        }


        //뒤로가기 버튼
        paintingDiaryBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        mindButton.setOnClickListener {
            if(editStatus == true) {
                val intent = Intent(this, MindActivity::class.java)
                intent.putExtra("date", year + "_" + month + "_" + date);
                startActivityForResult(intent, 2)
            }
        }

        saveButton.setOnClickListener {
            save()
            editButton.visibility = View.VISIBLE
            deleteButton.visibility = View.VISIBLE
            saveButton.visibility = View.GONE
            editStatus = false
        }

        editButton.setOnClickListener {
            titleEdit.isEnabled = true
            editStatus = true
            saveButton.visibility = View.VISIBLE
            editButton.visibility = View.GONE
            deleteButton.visibility = View.GONE
        }

        deleteButton.setOnClickListener {
            delete()
            finish() //인텐트 종료
            overridePendingTransition(0, 0) //인텐트 효과 없애기
            val intent = getIntent() //인텐트
            startActivity(intent) //액티비티 열기
            overridePendingTransition(0, 0) //인텐트 효과 없애기

        }

        writeTextView.setOnClickListener {
            if(editStatus) {
                val intent = Intent(this, WritingActivity::class.java)
                intent.putExtra("date", year + "_" + month + "_" + date);
                startActivityForResult(intent, 3)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                loadCachePainting()
            }
            else if(requestCode == 2) {
                loadCacheMind()
            }
            else if(requestCode == 3) {
                loadCacheContent()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap) : String {
        val fileName = System.currentTimeMillis().toString() + ".png" // 파일이름 현재시간.png

        /*
        * ContentValues() 객체 생성.
        * ContentValues는 ContentResolver가 처리할 수 있는 값을 저장해둘 목적으로 사용된다.
        * */
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "PICTURES/Grime") // 경로 설정
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName) // 파일이름을 put해준다.
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1) // 현재 is_pending 상태임을 만들어준다.
            // 다른 곳에서 이 데이터를 요구하면 무시하라는 의미로, 해당 저장소를 독점할 수 있다.
        }

        // 이미지를 저장할 uri를 미리 설정해놓는다.
        val uri = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if(uri != null) {
                val image = contentResolver.openFileDescriptor(uri, "w", null)
                // write 모드로 file을 open한다.

                if(image != null) {
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    //비트맵을 FileOutputStream를 통해 compress한다.
                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장소 독점을 해제한다.
                    contentResolver.update(uri, contentValues, null, null)
                }
            }
        } catch(e: FileNotFoundException) {
            Log.e("error", "error: " + e.message)
        } catch (e: IOException) {
            Log.e("error", "error: " + e.message)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        } finally {
            return fileName
        }
    }

    fun saveTempStatus() {
        val key = year + "_" + month + "_" + date
        val file = filesDir.path + "/" + "status.json"
        val loadedData = FileUtil.LoadFile(file)
        var json : JSONObject
        if(loadedData != null)
            json = JSONObject(loadedData)
        else
            json = JSONObject()
        try {
            if (json.getString(key) == "completed" || json.getString(key) == "editing")
                json.put(key, "editing")
            else
                json.put(key, "temp")
        } catch(e: Exception) {
            json.put(key, "temp")
        } finally {
            FileUtil.SaveFile(file, json.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun shareImage() {
        val fileName = year + "_" + month + "_" + date + ".png"
        val bitmap = BitmapFactory.decodeFile(filesDir.path + "/" + fileName)
        val fileName2 = saveImageOnAboveAndroidQ(bitmap)

        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path + "/Grime/" + fileName2)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND

        val photoURI = FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName,
            file
        )
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI)
        shareIntent.type = "image/png"
        startActivity(Intent.createChooser(shareIntent, "Grime 그림 공유"))
    }

    fun delete() {
        deleteStatus()
        deleteTitle()
        deletePaint()
        deleteMind()
        deleteContent()
        deleteCacheTitle()
        deleteCachePaint()
        deleteCacheMind()
        deleteCacheContent()
    }

    fun deleteStatus() {
        try {
            val key = year + "_" + month + "_" + date
            val file = filesDir.path + "/" + "status.json"
            val loadedData = FileUtil.LoadFile(file)
            var json: JSONObject
            if (loadedData != null) {
                json = JSONObject(loadedData)
                json.remove(key)
                FileUtil.SaveFile(file, json.toString())

            }
        } catch(e : Exception) {
            Log.e("error", "delete status error: " + e.message)
        }
    }

    fun deleteTitle() {
        try {
            val key = year + "_" + month + "_" + date
            val file = filesDir.path + "/" + "title.json"
            val loadedFile = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedFile != null) {
                json = JSONObject(loadedFile)
                json.remove(key)
                FileUtil.SaveFile(file, json.toString())
            }
        } catch (e : Exception) {
            Log.e("error", "delete title error: " + e.message)
        }
    }

    fun deletePaint() {
        try {
            val fileName = year + "_" + month + "_" + date + ".png"
            val file = File(filesDir.path + "/" + fileName)
            file.delete()
        } catch (e : Exception) {
            Log.e("error", "delete paint error: " + e.message)
        }
    }

    fun deleteMind() {
        try {
            val key = year + "_" + month + "_" + date
            val file = filesDir.path + "/" + "mind.json"
            val loadedFile = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedFile != null) {
                json = JSONObject(loadedFile)
                json.remove(key)
                FileUtil.SaveFile(file, json.toString())
            }

        } catch (e: Exception) {
            Log.e("error", "delete mind error: " + e.message)
        }
    }

    fun deleteContent() {
        try {
            val file = File(filesDir.path + "/" + year + "_" + month + "_" + date + "_content" + ".txt")
            file.delete()
        } catch (e: Exception) {
            Log.e("error", "delete content error: " + e.message)
        }
    }

    fun deleteCacheTitle() {
        try {
            val key = year + "_" + month + "_" + date
            val file = cacheDir.path + "/" + "title.json"
            val loadedFile = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedFile != null) {
                json = JSONObject(loadedFile)
                json.remove(key)
                FileUtil.SaveFile(file, json.toString())
            }
        } catch (e : Exception) {
            Log.e("error", "delete cache title error: " + e.message)
        }
    }

    fun deleteCachePaint() {
        try {
            val fileName = year + "_" + month + "_" + date + ".png"
            val file = File(cacheDir.path + "/" + fileName)
            file.delete()
        } catch (e : Exception) {
            Log.e("error", "delete cache paint error: " + e.message)
        }
    }

    fun deleteCacheMind() {
        try {
            val key = year + "_" + month + "_" + date
            val file = cacheDir.path + "/" + "mind.json"
            val loadedFile = FileUtil.LoadFile(file)
            var json : JSONObject
            if(loadedFile != null) {
                json = JSONObject(loadedFile)
                json.remove(key)
                FileUtil.SaveFile(file, json.toString())
            }

        } catch (e: Exception) {
            Log.e("error", "delete cache mind error: " + e.message)
        }
    }

    fun deleteCacheContent() {
        try {
            val file = File(cacheDir.path + "/" + year + "_" + month + "_" + date + "_content" + ".txt")
            file.delete()
        } catch (e: Exception) {
            Log.e("error", "delete cache content error: " + e.message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun save() {
        saveTitle()
        savePainting()
        saveMind()
        saveContent()
        saveStatus()
    }

    fun saveStatus() {
        val key = year + "_" + month + "_" + date
        val file = filesDir.path + "/" + "status.json"
        val loadedData = FileUtil.LoadFile(file)
        var json : JSONObject
        if(loadedData != null)
            json = JSONObject(loadedData)
        else
            json = JSONObject()
        json.put(key, "completed")
        FileUtil.SaveFile(file, json.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveTitle() {
        try {
            val fileName = "title.json"
            Files.copy(Path(cacheDir.path + "/" + fileName), Path(filesDir.path + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun savePainting() {
        try {
            val fileName = year + "_" + month + "_" + date + ".png"
            Files.copy(Path(cacheDir.path + "/" + fileName), Path(filesDir.path + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveMind() {
        try {
            val fileName = "mind.json"
            Files.copy(Path(cacheDir.path + "/" + fileName), Path(filesDir.path + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveContent() {
        try {
            val fileName = year + "_" + month + "_" + date + "_content.txt"
            Files.copy(Path(cacheDir.path + "/" + fileName), Path(filesDir.path + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            Log.e("error", "error: " + e.message)
        }
    }

    fun saveCacheTitle(title : String) {
        val key = year + "_" + month + "_" + date
        val file = cacheDir.path + "/" + "title.json"
        val loadedData = FileUtil.LoadFile(file)
        var json : JSONObject
        if(loadedData != null)
            json = JSONObject(loadedData)
        else
            json = JSONObject()
        json.put(key, title)
        FileUtil.SaveFile(file, json.toString())
    }

    fun load() {
        loadStatus()
        if(!loadStatus[0])
            loadTitle()
        if(!loadStatus[1])
            loadPaint()
        if(!loadStatus[2])
            loadMind()
        if(!loadStatus[3])
            loadContent()
    }

    fun loadStatus() {
        try {
            val key = year + "_" + month + "_" + date
            val file = filesDir.path + "/" + "status.json"
            val loadedFile = FileUtil.LoadFile(file)
            val json = JSONObject(loadedFile)
            status = json.getString(key)
        } catch (e : Exception) {
            Log.e("error", "load status error: " + e.message)
        }
    }

    fun loadTitle() {
        try {
            val key = year + "_" + month + "_" + date
            val file = filesDir.path + "/" + "title.json"
            val loaedFile = FileUtil.LoadFile(file)
            val json = JSONObject(loaedFile)
            titleEdit.setText(json.getString(key))
        } catch (e : Exception) {
            Log.e("error", "load title error: " + e.message)
        }
    }

    fun loadPaint() {
        try {
            val fileName = year + "_" + month + "_" + date + ".png"
            val bitmap = BitmapFactory.decodeFile(filesDir.path + "/" + fileName)
            paint.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("error", "load paint error: " + e.message)
        } catch (e : FileNotFoundException) {
            Log.e("error", "load cache content error: " + e.message)
        }
    }

    fun loadMind() {
        try {
            val key = year + "_" + month + "_" + date
            val file = filesDir.path + "/" + "mind.json"
            val loaedFile = FileUtil.LoadFile(file)
            val json = JSONObject(loaedFile)
            val mind = json.getString(key)
            if (mind == "angry")
                mindButton.setImageResource(R.drawable.angry)
            else if (mind == "sad")
                mindButton.setImageResource(R.drawable.sad)
            else if (mind == "happy")
                mindButton.setImageResource(R.drawable.happy)
            else if (mind == "soso")
                mindButton.setImageResource(R.drawable.soso)
            else if (mind == "delight")
                mindButton.setImageResource(R.drawable.delight)
        } catch (e: Exception) {
            Log.e("error", "load mind error: " + e.message)
        }
    }

    fun loadContent() {
        try {
            val file = filesDir.path + "/" + year + "_" + month + "_" + date + "_content" + ".txt"
            val loaedFile = FileUtil.LoadFile(file)
            writeTextView.setText(loaedFile)
        } catch (e: Exception) {
            Log.e("error", "load content error: " + e.message)
        }
    }

    fun loadCache() {
        loadCacheTitle()
        loadCachePainting()
        loadCacheMind()
        loadCacheContent()
    }

    fun loadCacheTitle() {
        try {
            val key = year + "_" + month + "_" + date
            val file = cacheDir.path + "/" + "title.json"
            val loadedFile = FileUtil.LoadFile(file)
            val json = JSONObject(loadedFile)
            val title = json.getString(key)
            titleEdit.setText(title)
            loadStatus[0] = true
        } catch (e : Exception) {
            Log.e("error", "load cache title error: " + e.message)
            loadStatus[0] = false
        }
    }

    fun loadCachePainting() {
        try {
            val fileName = year + "_" + month + "_" + date + ".png"
         val bitmap = BitmapFactory.decodeFile(cacheDir.path + "/" + fileName)
          paint.setImageBitmap(bitmap)
            loadStatus[1] = true
        } catch (e: Exception) {
            Log.e("error", "load cache painting error: " + e.message)
            loadStatus[1] = false
        } catch (e : FileNotFoundException) {
            Log.e("error", "load cache painting error: " + e.message)
            loadStatus[1] = false
        }
    }

    fun loadCacheMind() {
        try {
            val key = year + "_" + month + "_" + date
            val file = cacheDir.path + "/" + "mind.json"
            val loaedFile = FileUtil.LoadFile(file)
            val json = JSONObject(loaedFile)
            val mind = json.getString(key)
            if (mind == "angry")
                mindButton.setImageResource(R.drawable.angry)
            else if (mind == "sad")
                mindButton.setImageResource(R.drawable.sad)
            else if (mind == "happy")
                mindButton.setImageResource(R.drawable.happy)
            else if (mind == "soso")
                mindButton.setImageResource(R.drawable.soso)
            else if (mind == "delight")
                mindButton.setImageResource(R.drawable.delight)
            loadStatus[2] = true
        } catch (e: Exception) {
            Log.e("error", "load cache mind error: " + e.message)
            loadStatus[2] = false
        }
    }

    fun loadCacheContent() {
        try {
            val file = cacheDir.path + "/" + year + "_" + month + "_" + date + "_content" + ".txt"
            val loadedFile = FileUtil.LoadFile(file)
            writeTextView.setText(loadedFile)
            loadStatus[3] = loadedFile != null
        } catch (e: Exception) {
            Log.e("error", "load cache content error: " + e.message)
            loadStatus[3] = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onDataPass(data: String?) {
        if(data == "imageSave") {
            val fileName = year + "_" + month + "_" + date + ".png"
            val bitmap = BitmapFactory.decodeFile(filesDir.path + "/" + fileName)
            saveImageOnAboveAndroidQ(bitmap)
            Toast.makeText(this, "PICTURES/Grime에 저장이 완료되었습니다", Toast.LENGTH_SHORT).show()
        } else if(data == "shareImage") {
            shareImage()
        }
    }
}