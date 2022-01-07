package com.scnu.blockchain_based_im_app

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*
import android.view.animation.AlphaAnimation

import android.view.animation.Animation
import kotlinx.android.synthetic.main.activity_login.*


class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGHT = 2000 // 两秒后进入系统

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        var closed=false//做标记，看用户是否点击“跳过”来结束

        val zhangch: Animation = AlphaAnimation(1.0f, 0.6f)
        zhangch.setDuration(2000)
        image_start.startAnimation(zhangch)

        //用户点击“跳过”即打开主页
        btn_stop.setOnClickListener {
            closed=true
            val mainIntent = Intent(
                this@SplashActivity,
                LoginActivity::class.java
            )
            this@SplashActivity.startActivity(mainIntent)
            finish()
        }

        //延迟打开主页面
        Handler().postDelayed({
            if(!closed){//要是用户点击跳过就不再执行此
                val mainIntent = Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
                this@SplashActivity.startActivity(mainIntent)
                finish()
            }
        }, SPLASH_DISPLAY_LENGHT.toLong())

    }
}