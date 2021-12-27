package com.scnu.blockchain_based_im_app.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.R

class ChatAdapter(val activity: FragmentActivity, val context: Context, val chatsList: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chatImage: ImageView = view.findViewById(R.id.chatImage)
        val chatName: TextView = view.findViewById(R.id.chatName)
        val chatLastMessage: TextView = view.findViewById(R.id.chatLastMessage)
        val chatLastTime: TextView = view.findViewById(R.id.chatLastTime)
        val chatLayout: LinearLayout = view.findViewById(R.id.chatLayout)
        val notificationOff: ImageView = view.findViewById(R.id.notificationOff)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            val chat = chatsList[position]
            val intent = Intent(context, MsgActivity::class.java)
            intent.putExtra("chatterID", chat.chatterID)
            context.startActivity(intent)
        }
        return viewHolder
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatsList[position]
        holder.chatImage.setImageBitmap(chat.chatterBitmap)
        holder.chatName.text = chat.chatterName
        holder.chatLastMessage.text = chat.lastMessage
        holder.chatLastTime.text = chat.lastTime
        if(chat.stickOnTop == 1) {
            val resources = activity.baseContext.resources
            holder.chatLayout.background = resources.getDrawable(R.drawable.dark_underline)
        }
        if(chat.msgIgnore == 1) {
            holder.notificationOff.visibility = ImageView.VISIBLE
        }
    }

    override fun getItemCount() = chatsList.size
}