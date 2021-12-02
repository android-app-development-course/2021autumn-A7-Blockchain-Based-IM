package com.scnu.blockchain_based_im_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        /*val prefs=getSharedPreferences("userDB", Context.MODE_PRIVATE)
        val isRemenber=prefs.getBoolean("remenberPSD",false)
        if(isRemenber){
            val un0=prefs.getString("name","")
            val psd0=prefs.getString("psd","")
            userName.setText(un0)
            password.setText(psd0)
        }
        else{
            userName.setText("")
            password.setText("")
        }
        login.setOnClickListener {
            if(protocol.isChecked){
                //val un=userName.text.toString()
                val un="莫小叉"
                val psd=password.text.toString()
                //val prefs=getSharedPreferences("userDB", Context.MODE_PRIVATE)
                val un0=prefs.getString("name","")
                val psd0=prefs.getString("psd","")
                if(un==un0&&psd==psd0){
                    val intent=Intent(this,Welcome::class.java)
                    if(isRemenber){
                        val editor=getSharedPreferences("userDB",Context.MODE_PRIVATE).edit()
                        editor.putBoolean("remenberPSD",false)
                        editor.apply()
                    }
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"账号或密码错误",Toast.LENGTH_LONG).show()//account or password error!
                }
            }
            else{
                Toast.makeText(this,"请阅读并同意用户协议",Toast.LENGTH_LONG).show()//please read ang agree to the user agreement
            }
        }*/
        login.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        register.setOnClickListener {
            val intent=Intent(this, Register::class.java)
            startActivity(intent)
        }
    }
}