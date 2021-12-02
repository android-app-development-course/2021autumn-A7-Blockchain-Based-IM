package com.scnu.blockchain_based_im_app.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scnu.blockchain_based_im_app.R

class BackUpChatHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "备份聊天记录"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_up_chat_history)
    }
}