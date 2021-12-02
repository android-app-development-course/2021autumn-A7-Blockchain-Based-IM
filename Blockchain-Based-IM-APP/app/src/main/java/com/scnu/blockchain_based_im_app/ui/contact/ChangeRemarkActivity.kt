package com.scnu.blockchain_based_im_app.ui.contact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.activity_change_remark.*

class ChangeRemarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setContentView(R.layout.activity_change_remark)


        changeReturn.setOnClickListener {
            finish()
        }
    }

}