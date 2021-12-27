package com.scnu.blockchain_based_im_app.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_msg.*

class MsgActivity : AppCompatActivity(), View.OnClickListener {

    private val msgList = ArrayList<Msg>()
    private var adapter: MsgAdapter? = null
    private val dbHelper = MyDatabaseHelper(this, "IM_app.db", 2)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg)
        supportActionBar?.hide()

        btn_send.setOnClickListener(this)
        btn_back_to_chat.setOnClickListener(this)
        btn_photo.setOnClickListener(this)
        btn_msg_detail.setOnClickListener(this)

        //长按语音图标
        btn_voice.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> ll_record.visibility = View.VISIBLE
                MotionEvent.ACTION_UP -> ll_record.visibility = View.GONE
            }
            true
        }
    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        val thisFriendID = intent.getStringExtra("chatterID")
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from friendship where ownerID=? and friendID=?", arrayOf(MainActivity.userID, thisFriendID))
        if(cursor.moveToFirst()) {
            tv_name.text = cursor.getString(cursor.getColumnIndex("note"))
        }
        cursor.close()

        initMsg(thisFriendID)
        adapter = MsgAdapter(msgList)
        val linearLayoutManager = LinearLayoutManager(this)
        msgRecyclerView.layoutManager = linearLayoutManager
        msgRecyclerView.adapter = adapter

    }

    override fun onClick(v: View?) {
        val thisFriendID = intent.getStringExtra("chatterID")
        when (v) {
            btn_send -> {
                val content = edt_msg.text.toString()
                if (content.isNotEmpty()) {
                    val msg = Msg(content, Msg.TYPE_SEND)
                    msgList.add(msg)
                    //通知列表有数据插入
                    adapter?.notifyItemInserted(msgList.size - 1)
                    //把数据定位到最后一行
                    msgRecyclerView.scrollToPosition(msgList.size - 1)
                    //清空数据
                    edt_msg.setText("")

                    val db = dbHelper.writableDatabase
                    val timeMills = System.currentTimeMillis()
                    val time:CharSequence= DateFormat.format("yyyy"+"/"+"MM"+"/"+"dd"+" "+"HH:mm:ss", timeMills)
                    db.execSQL("insert into msg_content values(?,?,?,?,?)", arrayOf(MainActivity.userID, thisFriendID, content, Msg.TYPE_SEND, time))
                    db.execSQL("update chat set lastMessage=?, lastTime=? where ownerID=? and friendID=?", arrayOf(content, time, MainActivity.userID, thisFriendID))

                    db.execSQL("insert into msg_content values(?,?,?,?,?)", arrayOf(thisFriendID, MainActivity.userID, content, Msg.TYPE_RECEIVED, time))
                    val cursor = db.rawQuery("select * from chat where ownerID=? and friendID=?", arrayOf(thisFriendID, MainActivity.userID))
                    if(!cursor.moveToFirst()) {
                        db.execSQL("insert into chat values(?,?,?,?,?,?)", arrayOf(thisFriendID, MainActivity.userID, "", time, 0, 0))
                    }
                    cursor.close()
                    db.execSQL("update chat set lastMessage=?, lastTime=? where ownerID=? and friendID=?", arrayOf(content, time, thisFriendID, MainActivity.userID))
                } else {
                    Toast.makeText(this, "不能发送空消息", Toast.LENGTH_SHORT).show()
                }
            }

            btn_back_to_chat -> {
                finish()
            }

            //点击右上角的更多按钮
            btn_msg_detail -> {
                showPopup(v)
            }

            btn_photo -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivity(intent)
            }
        }
    }

    @SuppressLint("Range")
    private fun initMsg(friendID: String?) {
        msgList.clear()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from msg_content where ownerID=? and friendID=? order by sendTime", arrayOf(MainActivity.userID,friendID))
        if(cursor.moveToFirst()) {
            do {
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val type = cursor.getInt(cursor.getColumnIndex("msgType"))
                msgList.add(Msg(content, type))
            }while (cursor.moveToNext())
        }
    }

    @SuppressLint("Range")
    private fun showPopup(view : View?) {
        val thisFriendID = intent.getStringExtra("chatterID")
        val popup: PopupMenu = PopupMenu(this, view)
        popup.inflate(R.menu.msg_more_menu)
        val stickOnTopMenuItem : MenuItem? = popup.menu.findItem(R.id.stick_chat_on_top)
        val msgIgnoreMenuItem : MenuItem? = popup.menu.findItem(R.id.msg_ignore)
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("select * from chat where ownerID=? and friendID=?", arrayOf(MainActivity.userID,thisFriendID))
        if(!cursor.moveToFirst()) return
        val stickOnTop = cursor.getInt(cursor.getColumnIndex("stickOnTop"))
        val msgIgnore = cursor.getInt(cursor.getColumnIndex("msgIgnore"))
        //判断当前是否已置顶
        if(stickOnTop == 1) {
            stickOnTopMenuItem?.title = "取消置顶"
        }
        else if(stickOnTop == 0) {
            stickOnTopMenuItem?.title = "置顶聊天"
        }
        //判断当前是否已免打扰
        if(msgIgnore == 1) {
            msgIgnoreMenuItem?.title = "取消免打扰"
        }
        else if(msgIgnore == 0) {
            msgIgnoreMenuItem?.title = "消息免打扰"
        }
        cursor.close()

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.stick_chat_on_top -> {
                    if(stickOnTop == 1) {
                        db.execSQL("update chat set stickOnTop = 0 where ownerID=? and friendID=?", arrayOf(MainActivity.userID,thisFriendID))
                    }
                    else if(stickOnTop == 0){
                        db.execSQL("update chat set stickOnTop = 1 where ownerID=? and friendID=?", arrayOf(MainActivity.userID,thisFriendID))
                    }
                }
                R.id.msg_ignore -> {
                    if(msgIgnore == 1) {
                        db.execSQL("update chat set msgIgnore = 0 where ownerID=? and friendID=?", arrayOf(MainActivity.userID,thisFriendID))
                    }
                    else if(msgIgnore == 0){
                        db.execSQL("update chat set msgIgnore = 1 where ownerID=? and friendID=?", arrayOf(MainActivity.userID,thisFriendID))
                    }
                }
            }
            true
        })

        popup.show()
    }

}