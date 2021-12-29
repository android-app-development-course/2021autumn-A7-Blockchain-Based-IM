package com.scnu.blockchain_based_im_app.ui.contact

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_friend.*
import kotlinx.android.synthetic.main.activity_new_friend.*

class NewFriendActivity : AppCompatActivity() {

    private val userID = MainActivity.userID
    private val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)
    private val friendRequestList= ArrayList<Friend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_new_friend)

        newFriendReturn.setOnClickListener {
            finish()
        }

    }

    @SuppressLint("Range")
    override fun onResume() {
        Log.d("xiangge","onresume")
        super.onResume()
        friendRequestList.clear()
        val db:SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from friend_request where respondentID=$userID",null)
        if(cursor.moveToFirst()) {
            do {
                val applicantID = cursor.getString(cursor.getColumnIndex("applicantID"))
                val cursor2 = db.rawQuery("select * from user where id=$applicantID",null)
                if(cursor2.moveToFirst()) {
                    val applicantName = cursor2.getString(cursor2.getColumnIndex("name"))
                    val applicantPhotoByteChar: ByteArray = cursor2.getBlob(cursor2.getColumnIndex("profile_photo"))
                    val applicantPhotoBitmap: Bitmap = BitmapFactory.decodeByteArray(applicantPhotoByteChar,0,applicantPhotoByteChar.size)
                    friendRequestList.add(Friend(applicantID, applicantName, applicantPhotoBitmap))
                }
                cursor2.close()
            } while (cursor.moveToNext())
        }
        cursor.close()

        val layoutManager = LinearLayoutManager(this)
        newFriendRecyclerView.layoutManager = layoutManager
        val adapter = FriendRequestAdapter(this, friendRequestList)
        newFriendRecyclerView.adapter = adapter
    }

}