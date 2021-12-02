package com.scnu.blockchain_based_im_app.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_msg_detail.*

class MsgDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg_detail)
        supportActionBar?.hide()
        btn_back_to_msg.setOnClickListener {
            finish()
        }
    }
}