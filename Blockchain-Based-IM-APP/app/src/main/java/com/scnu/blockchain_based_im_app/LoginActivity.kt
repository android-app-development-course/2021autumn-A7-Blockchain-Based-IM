package com.scnu.blockchain_based_im_app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        login.setOnClickListener {
            val id =  userID.text.toString()
            val password = password.text.toString()
            var isLoginSucceeded = false
            var msg: String = ""
            when {
                id.isEmpty() -> msg = "id不能为空"
                password.isEmpty() -> msg = "密码不能为空"
                else -> {
                    val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)
                    val db = dbHelper.readableDatabase
                    val cursor = db.rawQuery("select password from user where id=?", arrayOf(id))
                    if(cursor.moveToFirst()) {
                        if(password == cursor.getString(cursor.getColumnIndex("password"))) {
                            isLoginSucceeded = true
                        }
                        else {
                            msg = "密码错误！"
                        }
                    }
                    else {
                        msg = "此id不存在！"
                    }
                    cursor.close()
                }
            }

            if(isLoginSucceeded) {
                //如果登录成功，进入用户界面。
                val intent=Intent(this, MainActivity::class.java)
                intent.putExtra("userID", id)
                startActivity(intent)
            }
            else {
                //如果登录失败，显示提示框，说明失败原因。
                AlertDialog.Builder(this).apply {
                    setTitle("提示")
                    setMessage(msg)
                    setCancelable(false)
                    setPositiveButton("确定", null)
                    show()
                }
            }

        }

        register.setOnClickListener {
            val intent=Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}