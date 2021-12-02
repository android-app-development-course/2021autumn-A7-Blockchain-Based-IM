package com.scnu.blockchain_based_im_app.ui.contact

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_friend.*



class FriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //注意顺序问题 隐藏标题栏
        setContentView(R.layout.activity_friend)

        val position=intent.getIntExtra("pos",0)//传来数组下标
        //Toast.makeText(this,position.toString(), Toast.LENGTH_SHORT).show()

        friendReturn.setOnClickListener {
            finish()
        }

        deleteFriend.setOnClickListener {
            AlertDialog.Builder(this)
                //.setMessage("成功")
                .setTitle("确定删除好友？")
                .setPositiveButton("确定删除", DialogInterface.OnClickListener { dialogInterface, i ->
                    //Toast.makeText(this,"对话框显示成功",Toast.LENGTH_LONG).show()
                })
                .setNeutralButton("取消", null)
                .create()
                .show()
        }

        remark.setOnClickListener {
            val intent= Intent(this,ChangeRemarkActivity::class.java)
            startActivity(intent)
        }
    }
}