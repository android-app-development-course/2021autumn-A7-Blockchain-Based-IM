package com.scnu.blockchain_based_im_app.ui.setting

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_recover_chat_history.*

class RecoverChatHistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "恢复聊天记录"
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_recover_chat_history)

        Return.setOnClickListener {
            finish()
        }

    }
}