package com.scnu.blockchain_based_im_app

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.register.*
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class Register : AppCompatActivity() {

    private val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        setContentView(R.layout.register)
        supportActionBar?.hide()

        btn_register_back_to_login.setOnClickListener {
            finish()
        }

        registerBtn.setOnClickListener {
            val id=userID.text.toString()
            val name=name.text.toString()
            val psd=password.text.toString()
            val rePsd=rePassword.text.toString()
            val msg : String = when {
                id.isEmpty() -> "ID不能为空！"
                hasSpace(id)->"ID不能含有空格"
                name.isEmpty() -> "昵称不能为空！"
                hasSpace(name)->"昵称不能含有空格"
                psd .isEmpty() -> "密码不能为空！"
                psd != rePsd -> "两次密码不一致！"
                psd.toString().length<8 ->"请输入至少8位密码！"
                else -> {
                    if(existID(id)) {
                        "id已被注册"
                    }
                    else {
                        addUser(id, name, psd)
                        "注册成功"
                    }
                }
            }

            //显示提示框
            /*AlertDialog.Builder(this).apply {
                setTitle("提示")
                setMessage(msg)
                setCancelable(false)
                setPositiveButton("确定") {_,_ ->
                    if(msg == "注册成功") {
                        //当注册成功时，退出注册界面，返回主界面。
                        finish()
                    }
                }
                show()
            }*/

            val builder = AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定"){_,_ ->
                    if(msg == "注册成功") {
                        //当注册成功时，退出注册界面，返回主界面。
                        finish()
                    }
                }
                .show()
            //圆角
            builder.getWindow()?.setBackgroundDrawableResource(R.drawable.circle_list)

            // 设置对话框的位置偏下
            /*val window: Window? = builder.window
            val wlp: WindowManager.LayoutParams = window!!.getAttributes()
            wlp.gravity = Gravity.BOTTOM
            val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display: Display = wm.defaultDisplay
            wlp.width = display.getWidth()
            window?.setAttributes(wlp)*/

        }
    }

    //判断用户是否已存在
    private fun existID(id: String) : Boolean{
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from user where id=?", arrayOf(id))
        val isExist = cursor.moveToFirst()
        cursor.close()
        return isExist
    }

    //在数据库中添加一个新用户，包括帐号、密码、昵称、初始头像
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addUser(id: String, name:String, password: String) {
        val baos: ByteArrayOutputStream = ByteArrayOutputStream()
        val bitmap: Bitmap = resources.getDrawable(R.drawable.app_icon).toBitmap()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos)
        val db = dbHelper.writableDatabase
        db.execSQL("insert into user(id, name, password, profile_photo) values(?, ?, ?, ?)", arrayOf(id, name, password, baos.toByteArray()))
    }

    private fun hasSpace(un:String) : Boolean{
        val len=un.length
        var i=0
        while(i<len){
            if(un[i].equals(' '))
                return true
            i++
        }
        return false
    }
}