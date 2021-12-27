package com.scnu.blockchain_based_im_app.ui.setting

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.R

class SettingAdapter(val context: Context, val fragment: SettingFragment, val settingList: List<Setting>):
    RecyclerView.Adapter<SettingAdapter.ViewHolder>(){

    private val activity : MainActivity = context as MainActivity

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tex_label: TextView = view.findViewById(R.id.tex_label)
        //val tex_name: TextView = view.findViewById(R.id.tex_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.setting_item, parent, false)
        val viewHolder = ViewHolder(view)

        //点击查看
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            //选择语句打开不同activity
            when (position) {
                //修改昵称
                0 -> {
                    val intent = Intent(context,ChangeMyNameActivity::class.java)
                    context.startActivity(intent)
                }
                //设置头像
                1 -> {
                    val intent = Intent(context,SetProfilePictureActivity::class.java)
                    context.startActivity(intent)
                }
                //修改密码
                2 -> {
                    val intent = Intent(context,ChangePasswordActivity::class.java)
                    context.startActivity(intent)
                }
                //字体大小
                3 -> {
                    class SetFontSizeDialog : DialogFragment() {
                        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                            return activity?.let {
                                val builder = android.app.AlertDialog.Builder(it)
                                // Get the layout inflater
                                val inflater = requireActivity().layoutInflater;

                                // Inflate and set the layout for the dialog
                                // Pass null as the parent view because its going in the dialog layout
                                builder.setView(inflater.inflate(R.layout.dialog_set_font_size, null))
                                    // Add action buttons
                                    .setPositiveButton(R.string.OK,
                                        DialogInterface.OnClickListener { dialog, id ->
                                            // sign in the user ...
                                        })
                                    .setNegativeButton(R.string.cancel,
                                        DialogInterface.OnClickListener { dialog, id ->
                                            getDialog()?.cancel()
                                        })
                                builder.create()
                            } ?: throw IllegalStateException("Activity cannot be null")
                        }
                    }
                    SetFontSizeDialog().show(fragment.parentFragmentManager,"jige2")
                }
                //备份聊天记录
                4 -> {
                    val intent = Intent(context,BackUpChatHistoryActivity::class.java)
                    context.startActivity(intent)
                }
                //帮助
                5 -> {
                    val intent = Intent(context,HelperActivity::class.java)
                    context.startActivity(intent)
                }
                //反馈
                6 -> {
                    val intent = Intent(context,FeedbackActivity::class.java)
                    context.startActivity(intent)
                }
                //退出登录
                7 -> {
                    AlertDialog.Builder(context).apply {
                        setTitle("提示")
                        setMessage("确定要退出登录吗？")
                        setPositiveButton("确定") { _, _ ->
                            activity.finishMe()
                        }
                        setNegativeButton("取消",null)
                        show()
                    }
                }
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val settingItem =settingList[position]
        holder.tex_label.text = settingItem.label
    }

    override fun getItemCount(): Int = settingList.size
}