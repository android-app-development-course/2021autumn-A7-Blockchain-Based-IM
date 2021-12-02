package com.scnu.blockchain_based_im_app.ui.contact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.ListView
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_friend.*
import kotlinx.android.synthetic.main.activity_new_friend.*

class NewFriendActivity : AppCompatActivity() {

    private val friendList= ArrayList<Friend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //注意顺序问题 隐藏标题
        setContentView(R.layout.activity_new_friend)

        initFriend()
        val adapter=FriendAdapter(this,R.layout.new_friend,friendList)
        val newFriendListView: ListView =findViewById(R.id.newFriendListView)
        newFriendListView.adapter=adapter

        newFriendListView.setOnItemClickListener { _, _, position, _ ->
            val friend=friendList[position]
            //val str="wgnb"
            val intent= Intent(this,FriendActivity::class.java)//链接到好友主页
            intent.putExtra("pos",position)
            startActivity(intent)
        }

        newFriendReturn.setOnClickListener {
            finish()
        }

    }

    private fun initFriend(){
        repeat(10){

            friendList.add(Friend("莫小叉",R.drawable.temp_profile_picture))
            friendList.add(Friend("吴",R.drawable.temp_profile_picture))
            friendList.add(Friend("子龙",R.drawable.temp_profile_picture))
        }
    }

}