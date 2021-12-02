package com.scnu.blockchain_based_im_app.ui.chat

class Msg(val content: String, val type: Int) {

    //伴生对象，类似于java的静态方法
    companion object {
        const val TYPE_RECEIVED = 0
        const val TYPE_SEND = 1
    }
}
