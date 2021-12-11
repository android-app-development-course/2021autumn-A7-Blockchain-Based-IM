package com.scnu.blockchain_based_im_app.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_recover_chat_history.*

class RecoverChatHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "恢复聊天记录"
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_recover_chat_history)

        Return.setOnClickListener {
            finish()
        }

    }
}