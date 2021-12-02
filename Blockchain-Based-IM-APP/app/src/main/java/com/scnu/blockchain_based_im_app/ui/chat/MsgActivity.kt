package com.scnu.blockchain_based_im_app.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_msg.*

class MsgActivity : AppCompatActivity(), View.OnClickListener {

    private val msgList = ArrayList<Msg>()

    private var adapter: MsgAdapter? = null

//    @SuppressLint("HandlerLeak")
//    val handler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            when (msg.what) {
//                0 -> {
//                    val toString = msg.obj.toString()
//                    val msg = Msg(toString, Msg.TYPE_RECEIVED)
//                    msgList.add(msg)
//                    //通知列表有数据插入
//                    adapter.notifyItemInserted(msgList.size - 1)
//                    //把数据定位到最后一行
//                    msgRecyclerView.scrollToPosition(msgList.size - 1)
//                }
//            }
//        }
//    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg)
        supportActionBar?.hide()

        initData()
        adapter = MsgAdapter(msgList)
        val linearLayoutManager = LinearLayoutManager(this)
        msgRecyclerView.layoutManager = linearLayoutManager
        msgRecyclerView.adapter = adapter
        tv_name.text = intent.getStringExtra("chatName")
        btn_send.setOnClickListener(this)
        btn_back_to_chat.setOnClickListener(this)
        btn_voice.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> ll_record.visibility = View.VISIBLE
                MotionEvent.ACTION_UP -> ll_record.visibility = View.GONE
            }
            true
        }
        btn_photo.setOnClickListener(this)
        btn_msg_detail.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
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

                    //开启线程
//                    startThread(content)

                } else {
                    Toast.makeText(this, "不能发送空消息", Toast.LENGTH_SHORT).show()
                }
            }
            btn_back_to_chat -> {
                finish()
            }
            btn_msg_detail -> {
                val intent = Intent(this, MsgDetailActivity::class.java)
                startActivity(intent)
            }
            btn_photo -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivity(intent)
            }
        }
    }

//    //用来添加接收的数据
//    private fun startThread(str: String) {
//        Thread {
//            Thread.sleep(1000)
//            val message = Message()
//            message.what = 0
//            message.obj = str
//            handler.sendMessage(message)
//        }.start()
//    }

    private fun initData() {
        msgList.add(Msg("你好", Msg.TYPE_RECEIVED))
        msgList.add(Msg("你好，请问你是？", Msg.TYPE_SEND))
        msgList.add(Msg("我是你的新舍友。我叫James，很高兴认识你！", Msg.TYPE_RECEIVED))
    }

}