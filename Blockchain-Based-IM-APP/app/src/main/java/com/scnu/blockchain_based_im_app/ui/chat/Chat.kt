package com.scnu.blockchain_based_im_app.ui.chat

import android.graphics.Bitmap

class Chat(val chatterID: String, val chatterBitmap: Bitmap, val chatterName: String,
           val lastMessage: String, val lastTime: String, val stickOnTop: Int, val msgIgnore: Int)