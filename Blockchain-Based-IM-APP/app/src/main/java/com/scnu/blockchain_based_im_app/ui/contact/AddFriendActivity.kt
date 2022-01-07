package com.scnu.blockchain_based_im_app.ui.contact

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_addfriend.*

class AddFriendActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        supportActionBar?.hide()//隐藏标题栏
        setContentView(R.layout.activity_addfriend)

        val newFriendID = intent.getStringExtra("newFriendID")
        val newFriendIDText = "id: $newFriendID"
        tv_newFriendID.text = newFriendIDText

        val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)
        val db = dbHelper.writableDatabase
        var cursor = db.rawQuery("select * from user where id=?", arrayOf(newFriendID))
        if(cursor.moveToFirst()) {
            val newFriendNameText = "昵称: ${cursor.getString(cursor.getColumnIndex("name"))}"
            tv_newFriendName.text = newFriendNameText
            val photoByteChar:ByteArray = cursor.getBlob(cursor.getColumnIndex("profile_photo"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(photoByteChar,0,photoByteChar.size)
            newFriendPhoto.setImageBitmap(bitmap)
        }

        requestNewFriend.setOnClickListener {
            cursor = db.rawQuery("select * from friendship where ownerID=? and friendID=?", arrayOf(MainActivity.userID,newFriendID))
            if(cursor.moveToFirst()) {
                showDialog("对方已经是你的好友")
            }
            else {
                cursor = db.rawQuery("select * from friend_request where applicantID=? and respondentID=?", arrayOf(MainActivity.userID,newFriendID))
                if(cursor.moveToFirst()) {
                    showDialog("你已经向对方发送过好友申请，请耐心等待回复。")
                }
                else {
                    db.execSQL("insert into friend_request(applicantID,respondentID) values(?,?)",
                        arrayOf(MainActivity.userID,newFriendID))
                    showDialog("已发送好友请求，请等待回复。")
                }
            }
        }

        addFriendReturn.setOnClickListener {
            finish()
        }

        cursor.close()
    }

    private fun showDialog(msg: String) {
        /*AlertDialog.Builder(this).apply {
            setTitle("提示")
            setMessage(msg)
            setCancelable(false)
            setPositiveButton("确定", null)
            show()

        }*/
        val builder = AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("确定",null)
            .show()
        //圆角
        builder.getWindow()?.setBackgroundDrawableResource(R.drawable.circle_bottom_list)

        // 设置对话框的位置偏下
        val window: Window? = builder.window
        val wlp: WindowManager.LayoutParams = window!!.getAttributes()
        wlp.gravity = Gravity.BOTTOM
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        wlp.width = display.getWidth()
        window?.setAttributes(wlp)
    }
}