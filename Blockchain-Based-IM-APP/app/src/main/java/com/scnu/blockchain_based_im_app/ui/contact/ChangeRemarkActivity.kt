package com.scnu.blockchain_based_im_app.ui.contact

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AlertDialog
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_change_remark.*
import kotlinx.android.synthetic.main.activity_friend.*

class ChangeRemarkActivity : AppCompatActivity() {

    private val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)
    val userID = MainActivity.userID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setContentView(R.layout.activity_change_remark)

        val thisFriendID = intent.getStringExtra("friendID")

        changeRemark_ok.setOnClickListener {
            if(currentNote.text.isEmpty()) {
                AlertDialog.Builder(this).apply {
                    setTitle("提示")
                    setMessage("备注不能为空！")
                    setCancelable(false)
                    setPositiveButton("确定", null)
                    show()
                }
            }
            else {
                val newNote = currentNote.text.toString()
                val db = dbHelper.writableDatabase
                db.execSQL("update friendship set note=? where ownerID=? and friendID=?", arrayOf(newNote,userID,thisFriendID))
                finish()
            }
        }

        changeReturn.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        val thisFriendID = intent.getStringExtra("friendID")
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from friendship where ownerID=$userID and friendID=$thisFriendID",null)
        if(cursor.moveToFirst()) {
            val thisFriendNoteName = cursor.getString(cursor.getColumnIndex("note"))
            currentNote.setText(thisFriendNoteName)
        }
        cursor.close()
    }

}