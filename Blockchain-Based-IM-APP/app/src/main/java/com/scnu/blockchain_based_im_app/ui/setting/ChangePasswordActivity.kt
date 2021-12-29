package com.scnu.blockchain_based_im_app.ui.setting

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "修改密码"
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_change_password)

        button_ensure_change_password.setOnClickListener {
            val oldPasswordInput = edit_text_enter_old_password.text.toString()
            val newPasswordInput = edit_text_enter_new_password.text.toString()
            val newPasswordAgainInput = edit_text_enter_new_password_again.text.toString()
            val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("select * from user where id=?", arrayOf(MainActivity.userID))
            if(cursor.moveToFirst()) {
                val oldPassword = cursor.getString(cursor.getColumnIndex("password"))
                if(oldPassword != oldPasswordInput) {
                    showMyDialog("原密码错误")
                }
                else {
                    if(newPasswordInput.isEmpty() || newPasswordAgainInput.isEmpty()) {
                        showMyDialog("新密码不能为空")
                    }
                    else if(newPasswordInput != newPasswordAgainInput) {
                        showMyDialog("两次输入的新密码不一致")
                    }
                    else {
                        db.execSQL("update user set password=? where id=?", arrayOf(newPasswordInput,MainActivity.userID))
                        showMyDialog("修改成功")
                    }
                }
            }
            cursor.close()
        }

        Return.setOnClickListener {
            finish()
        }
    }

    private fun showMyDialog(msg: String) {
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
        builder.getWindow()?.setBackgroundDrawableResource(R.drawable.circle_list)
    }

}