package com.scnu.blockchain_based_im_app.ui.contact

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import com.scnu.blockchain_based_im_app.ui.chat.MsgActivity
import kotlinx.android.synthetic.main.activity_friend.*


class FriendActivity : AppCompatActivity() {

    private val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        setContentView(R.layout.activity_friend)

        val thisFriendID = intent.getStringExtra("friendID")

        friendReturn.setOnClickListener {
            finish()
        }

        //删除好友
        deleteFriend.setOnClickListener {

            /*AlertDialog.Builder(this)
                //.setMessage("成功")
                .setTitle("确定删除好友？")
                .setPositiveButton("确定删除", DialogInterface.OnClickListener { dialogInterface, i ->
//                    Toast.makeText(this,"对话框显示成功",Toast.LENGTH_LONG).show()
                    val db = dbHelper.writableDatabase
                    db.execSQL("delete from msg_content where ownerID=${MainActivity.userID} and friendID=$thisFriendID")
                    db.execSQL("delete from msg_content where ownerID=$thisFriendID and friendID=${MainActivity.userID}")
                    db.execSQL("delete from chat where ownerID=${MainActivity.userID} and friendID=$thisFriendID")
                    db.execSQL("delete from chat where ownerID=$thisFriendID and friendID=${MainActivity.userID}")
                    db.execSQL("delete from friendship where ownerID=${MainActivity.userID} and friendID=$thisFriendID")
                    db.execSQL("delete from friendship where ownerID=$thisFriendID and friendID=${MainActivity.userID}")
                    finish()
                })
                .setNeutralButton("取消", null)
                .create()
                .show()*/

            val builder = AlertDialog.Builder(this)
                //.setMessage("成功")
                .setTitle("删除联系人")
                .setMessage("将联系人删除，将同时删除与改联系人的聊天记录")
                .setPositiveButton("确定删除", DialogInterface.OnClickListener { dialogInterface, i ->
//                    Toast.makeText(this,"对话框显示成功",Toast.LENGTH_LONG).show()
                    val db = dbHelper.writableDatabase
                    db.execSQL("delete from msg_content where ownerID=${MainActivity.userID} and friendID=$thisFriendID")
                    db.execSQL("delete from msg_content where ownerID=$thisFriendID and friendID=${MainActivity.userID}")
                    db.execSQL("delete from chat where ownerID=${MainActivity.userID} and friendID=$thisFriendID")
                    db.execSQL("delete from chat where ownerID=$thisFriendID and friendID=${MainActivity.userID}")
                    db.execSQL("delete from friendship where ownerID=${MainActivity.userID} and friendID=$thisFriendID")
                    db.execSQL("delete from friendship where ownerID=$thisFriendID and friendID=${MainActivity.userID}")
                    finish()
                })
                .setNeutralButton("取消", null)
                .show()
            builder.getWindow()?.setBackgroundDrawableResource(R.drawable.circle_list)

            // 设置对话框的位置偏下
            val window: Window? = builder.window
            val wlp: WindowManager.LayoutParams = window!!.getAttributes()
            wlp.gravity = Gravity.BOTTOM
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display: Display = wm.defaultDisplay
            wlp.width = display.getWidth()
            window?.setAttributes(wlp)

        }

        //和该好友聊天（发消息）
        sendMessage.setOnClickListener {
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from chat where ownerID=? and friendID=?",
                arrayOf(MainActivity.userID, thisFriendID))
            if(!cursor.moveToFirst()) {
                val timeMills = System.currentTimeMillis()
                val time:CharSequence= DateFormat.format("yyyy"+"/"+"MM"+"/"+"dd"+" "+"HH:mm:ss", timeMills)
                db.execSQL("insert into chat(ownerID,friendID,lastMessage,lastTime,stickOnTop,msgIgnore) values(?,?,?,?,?,?)",
                    arrayOf(MainActivity.userID, thisFriendID, "", time, 0, 0))
            }
            cursor.close()
            val intent = Intent(this, MsgActivity::class.java)
            intent.putExtra("chatterID", thisFriendID)
            this.startActivity(intent)
        }

        //修改好友备注
        remark.setOnClickListener {
            val intent= Intent(this,ChangeRemarkActivity::class.java)
            intent.putExtra("friendID", thisFriendID)
            startActivity(intent)
        }
    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        val thisFriendID = intent.getStringExtra("friendID")
        val friendIDText = "id: $thisFriendID"
        friendID.text = friendIDText
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from user where id=$thisFriendID",null)
        if(cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val photoByteChar:ByteArray = cursor.getBlob(cursor.getColumnIndex("profile_photo"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(photoByteChar,0,photoByteChar.size)
            val friendNameText = "昵称: $name"
            friendName.text = friendNameText
            friendPhoto.setImageBitmap(bitmap)
        }
        cursor.close()
        val cursor2 = db.rawQuery("select * from friendship where ownerID=${MainActivity.userID} and friendID=$thisFriendID",null)
        if(cursor2.moveToFirst()) {
            val thisFriendNoteName = cursor2.getString(cursor2.getColumnIndex("note"))
            friendNoteName.text = thisFriendNoteName
        }
        cursor2.close()
    }

}