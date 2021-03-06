package com.scnu.blockchain_based_im_app.ui.contact

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_search_friend.*

class SearchFriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        setContentView(R.layout.activity_search_friend)
        supportActionBar?.hide()

        //让搜索图标包含在搜索框内
        sv_searchFriend.onActionViewExpanded()
        //取消一打开该界面就弹出输入键盘
        sv_searchFriend.clearFocus()

        //设置搜索框文本监听
        sv_searchFriend.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newString: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(queryString: String?): Boolean {
                if (queryString == MainActivity.userID) {
                    showDialog("这是自己的id")
                }
                else {
                    queryUser(queryString)
                }
                return true
            }
        })

        searchFriendReturn.setOnClickListener {
            finish()
        }
    }

    fun showDialog(msg: String) {
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

        // 设置对话框的位置偏下
        val window: Window? = builder.window
        val wlp: WindowManager.LayoutParams = window!!.getAttributes()
        wlp.gravity = Gravity.BOTTOM
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        wlp.width = display.getWidth()
        window?.setAttributes(wlp)
    }

    @SuppressLint("Range")
    fun queryUser(queryID: String?) {
        val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from user where id=?", arrayOf(queryID))
        if(cursor.moveToFirst()) {
            val intent = Intent(this, AddFriendActivity::class.java)
            intent.putExtra("newFriendID", queryID)
            startActivity(intent)
        }
        else {
            showDialog("不存在该用户")
        }
        cursor.close()
    }

}