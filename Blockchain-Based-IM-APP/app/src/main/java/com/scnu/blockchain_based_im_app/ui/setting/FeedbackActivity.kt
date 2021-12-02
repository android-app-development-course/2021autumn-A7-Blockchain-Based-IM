package com.scnu.blockchain_based_im_app.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity(){
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.title = "反馈"
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            val mailReceivers = arrayOf("admin@5t4r1i9ht.com")
            putExtra(Intent.EXTRA_EMAIL, mailReceivers)
            putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            putExtra(Intent.EXTRA_TEXT,"Enter your feedback here:")
        }
        startActivity(intent)
    }
}