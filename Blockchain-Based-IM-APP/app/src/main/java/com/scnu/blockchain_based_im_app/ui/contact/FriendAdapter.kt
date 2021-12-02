package com.scnu.blockchain_based_im_app.ui.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.scnu.blockchain_based_im_app.R

class FriendAdapter(context: Context, val resourceId:Int, data:List<Friend>):
    ArrayAdapter<Friend>(context, resourceId, data){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view= LayoutInflater.from(context).inflate(resourceId,parent,false)
        val friendImage: ImageView =view.findViewById(R.id.friendImage)
        val friendName: TextView =view.findViewById(R.id.friendName)
        val friend=getItem(position)
        if(friend!=null){
            friendImage.setImageResource(friend.imageId)
            friendName.text=friend.name
        }
        return view
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}