package com.scnu.blockchain_based_im_app.ui.setting

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.databinding.FragmentSettingBinding
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val userID = MainActivity.userID
    private val settingList=ArrayList<Setting>()

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        settingViewModel =
            ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var v:Setting=Setting("修改昵称")
        settingList.add(v)
        v=Setting("设置头像")
        settingList.add(v)
        v=Setting("修改密码")
        settingList.add(v)
        v=Setting("字体大小")
        settingList.add(v)
        v=Setting("备份聊天记录")
        settingList.add(v)
        v=Setting("帮助")
        settingList.add(v)
        v=Setting("反馈")
        settingList.add(v)
        v= Setting("退出登录")
        settingList.add(v)

        val layoutManager = LinearLayoutManager(requireContext())
        // 展示在主页
        settingRecycleView.layoutManager = layoutManager
        val adapter = SettingAdapter(requireContext(),this, settingList)
        settingRecycleView.adapter = adapter
    }

    @SuppressLint("Range")
    override fun onStart() {
        super.onStart()
        val dbHelper = MyDatabaseHelper(requireContext(), "IM_app.db", 2)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from user where id=$userID",null)
        if(cursor.moveToFirst()) {
            val userName = cursor.getString(cursor.getColumnIndex("name"))
            val userPhotoByteChar:ByteArray = cursor.getBlob(cursor.getColumnIndex("profile_photo"))
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(userPhotoByteChar,0,userPhotoByteChar.size)
            val myIDText = "id: $userID"
            val myNameText = "昵称: $userName"
            myID.text = myIDText
            myName.text = myNameText
            profile_photo.setImageBitmap(bitmap)
        }
        cursor.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}