package com.scnu.blockchain_based_im_app.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scnu.blockchain_based_im_app.R

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "修改密码"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
    }
}