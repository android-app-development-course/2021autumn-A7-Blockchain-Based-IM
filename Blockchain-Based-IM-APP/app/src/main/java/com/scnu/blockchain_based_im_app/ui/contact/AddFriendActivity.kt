package com.scnu.blockchain_based_im_app.ui.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_addfriend.*
import kotlinx.android.synthetic.main.activity_new_friend.*

class AddFriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); //注意顺序问题 隐藏标题
        setContentView(R.layout.activity_addfriend)

        addFriendReturn.setOnClickListener {
            finish()
        }
    }
}