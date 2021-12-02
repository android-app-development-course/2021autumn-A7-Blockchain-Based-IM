package com.scnu.blockchain_based_im_app.ui.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scnu.blockchain_based_im_app.R

class ChatAdapter(val context: Context, val chatsList: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chatImage: ImageView = view.findViewById(R.id.chatImage)
        val chatName: TextView = view.findViewById(R.id.chatName)
        val lastMessage: TextView = view.findViewById(R.id.lastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.absoluteAdapterPosition
            val chat = chatsList[position]
            val chatName = chat.name
            val intent = Intent(context, MsgActivity::class.java)
            intent.putExtra("chatName", chatName)
            context.startActivity(intent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatsList[position]
        holder.chatImage.setImageResource(chat.imageId)
        holder.chatName.text = chat.name
        holder.lastMessage.text = chat.lastMessage
    }

    override fun getItemCount() = chatsList.size
}