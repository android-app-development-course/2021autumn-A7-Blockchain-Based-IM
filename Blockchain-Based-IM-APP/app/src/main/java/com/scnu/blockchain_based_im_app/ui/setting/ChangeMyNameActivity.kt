package com.scnu.blockchain_based_im_app.ui.setting

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_change_my_name.*

class ChangeMyNameActivity : AppCompatActivity() {

    private val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        setContentView(R.layout.activity_change_my_name)
        supportActionBar?.hide()

        changeMyName_ok.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.execSQL("update user set name=? where id=?", arrayOf(myCurrentName.text.toString(),MainActivity.userID))
            finish()
        }

        changeMyNameReturn.setOnClickListener {
            finish()
        }

    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from user where id=?", arrayOf(MainActivity.userID))
        if(cursor.moveToFirst()) {
            myCurrentName.setText(cursor.getString(cursor.getColumnIndex("name")))
        }
        cursor.close()
    }

}