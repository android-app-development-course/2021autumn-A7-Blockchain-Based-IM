package com.scnu.blockchain_based_im_app

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.scnu.blockchain_based_im_app.R
import kotlinx.android.synthetic.main.register.*
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        supportActionBar?.hide()

        registerBtn.setOnClickListener {
            val un=userName.text.toString()
            val ph=phone.text.toString()
            val psd=password.text.toString()
            val rePsd=rePassword.text.toString()
            if(psd==rePsd){
                val editor=getSharedPreferences("userDB",Context.MODE_PRIVATE).edit()
                editor.putString("name",un)
                editor.putString("phone",ph)
                editor.putString("psd",psd)
                editor.putBoolean("remenberPSD",false)
                editor.apply()
                Toast.makeText(this,"注册成功",Toast.LENGTH_LONG).show()//register succeed
                finish()
            }
            else{
                Toast.makeText(this,"密码不一致",Toast.LENGTH_LONG).show()
            }
        }
    }
}