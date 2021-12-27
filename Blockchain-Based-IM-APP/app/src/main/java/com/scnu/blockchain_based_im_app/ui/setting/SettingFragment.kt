package com.scnu.blockchain_based_im_app.ui.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
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

//        val btnSetUserName: Button = requireView().findViewById(R.id.btn_set_user_name)
//        btnSetUserName.setOnClickListener {
//            class SetUserNameDialog : DialogFragment() {
//                override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//                    return activity?.let {
//                        val builder = AlertDialog.Builder(it)
//                        // Get the layout inflater
//                        val inflater = requireActivity().layoutInflater;
//
//                        // Inflate and set the layout for the dialog
//                        // Pass null as the parent view because its going in the dialog layout
//                        builder.setView(inflater.inflate(R.layout.dialog_set_user_name, null))
//                            // Add action buttons
//                            .setPositiveButton(R.string.OK,
//                                DialogInterface.OnClickListener { dialog, id ->
//                                    // sign in the user ...
//                                })
//                            .setNegativeButton(R.string.cancel,
//                                DialogInterface.OnClickListener { dialog, id ->
//                                    getDialog()?.cancel()
//                                })
//                        builder.create()
//                    } ?: throw IllegalStateException("Activity cannot be null")
//                }
//            }
//            SetUserNameDialog().show(parentFragmentManager,"jige")
//        }
//
//        val btnSetProfilePicture: Button = requireView().findViewById(R.id.btn_set_profile_picture)
//        btnSetProfilePicture.setOnClickListener {
//            val intentSetProfilePicture = Intent("com.scnu.scnu.blockchain_based_im_app.ACTION_START_SetProfilePictureActivity")
//            intentSetProfilePicture.addCategory("android.intent.category.DEFAULT")
//            startActivity(intentSetProfilePicture)
//        }
//
//        val btnChangePassword: Button = requireView().findViewById(R.id.btn_change_password)
//        btnChangePassword.setOnClickListener {
//            val intentChangePassword = Intent("com.scnu.scnu.blockchain_based_im_app.ACTION_START_ChangePasswordActivity")
//            intentChangePassword.addCategory("android.intent.category.DEFAULT")
//            startActivity(intentChangePassword)
//        }
//
//        val btnSetFontSize: Button = requireView().findViewById(R.id.btn_set_font_size)
//        btnSetFontSize.setOnClickListener {
//            class SetFontSizeDialog : DialogFragment() {
//                override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//                    return activity?.let {
//                        val builder = AlertDialog.Builder(it)
//                        // Get the layout inflater
//                        val inflater = requireActivity().layoutInflater;
//
//                        // Inflate and set the layout for the dialog
//                        // Pass null as the parent view because its going in the dialog layout
//                        builder.setView(inflater.inflate(R.layout.dialog_set_font_size, null))
//                            // Add action buttons
//                            .setPositiveButton(R.string.OK,
//                                DialogInterface.OnClickListener { dialog, id ->
//                                    // sign in the user ...
//                                })
//                            .setNegativeButton(R.string.cancel,
//                                DialogInterface.OnClickListener { dialog, id ->
//                                    getDialog()?.cancel()
//                                })
//                        builder.create()
//                    } ?: throw IllegalStateException("Activity cannot be null")
//                }
//            }
//            SetFontSizeDialog().show(this.parentFragmentManager,"jige2")
//        }
//
//        val btnBackUpChatHistory: Button = requireView().findViewById(R.id.btn_back_up_chat_history)
//        btnBackUpChatHistory.setOnClickListener {
//            val intentBackUpChatHistory = Intent("com.scnu.scnu.blockchain_based_im_app.ACTION_START_BackUpChatHistoryActivity")
//            intentBackUpChatHistory.addCategory("android.intent.category.DEFAULT")
//            startActivity(intentBackUpChatHistory)
//        }
//
//        val btnRecoverChatHistory: Button = requireView().findViewById(R.id.btn_recover_chat_history)
//        btnRecoverChatHistory.setOnClickListener {
//            val intentRecoverChatHistory = Intent("com.scnu.scnu.blockchain_based_im_app.ACTION_START_RecoverChatHistoryActivity")
//            intentRecoverChatHistory.addCategory("android.intent.category.DEFAULT")
//            startActivity(intentRecoverChatHistory)
//        }
//
//        val btnHelp: Button = requireView().findViewById(R.id.btn_help)
//        btnHelp.setOnClickListener {
//            val intentHelp = Intent("com.scnu.scnu.blockchain_based_im_app.ACTION_START_HelperActivity")
//            intentHelp.addCategory("android.intent.category.DEFAULT")
//            startActivity(intentHelp)
//        }
//
//        val btnFeedback: Button = requireView().findViewById(R.id.btn_feedback)
//        btnFeedback.setOnClickListener {
//            val intentFeedback = Intent("com.scnu.scnu.blockchain_based_im_app.ACTION_START_FeedbackActivity")
//            intentFeedback.addCategory("android.intent.category.DEFAULT")
//            startActivity(intentFeedback)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}