package com.scnu.blockchain_based_im_app.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scnu.blockchain_based_im_app.R


class MsgAdapter(var data: List<Msg>) : RecyclerView.Adapter<MsgAdapter.MsgViewHolder>() {
    //密封类
    sealed class MsgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class LeftVH(view: View) : MsgViewHolder(view) {
            val leftMsg: TextView = view.findViewById(R.id.tv_left_item)
        }
        class RightVH(view: View) : MsgViewHolder(view) {
            val rightMsg: TextView = view.findViewById(R.id.tv_right_item)
        }
    }

    override fun getItemViewType(position: Int) = data[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {
        val vh: MsgViewHolder
        if (viewType == Msg.TYPE_RECEIVED) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.msg_left_item, parent, false)
            vh = MsgViewHolder.LeftVH(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.msg_right_item, parent, false)
            vh = MsgViewHolder.RightVH(view)
        }
        return vh
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {
        val msg = data[position]
        when (holder) {
            is MsgViewHolder.LeftVH -> holder.leftMsg.text = msg.content
            is MsgViewHolder.RightVH -> holder.rightMsg.text = msg.content
        }
    }

}
