package com.scnu.blockchain_based_im_app.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "这是设置"
    }
    val text: LiveData<String> = _text
}