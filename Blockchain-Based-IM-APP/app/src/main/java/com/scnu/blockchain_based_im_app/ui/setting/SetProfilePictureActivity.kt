package com.scnu.blockchain_based_im_app.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.scnu.blockchain_based_im_app.R

class SetProfilePictureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.title = "设置头像"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_profile_picture)
    }
}