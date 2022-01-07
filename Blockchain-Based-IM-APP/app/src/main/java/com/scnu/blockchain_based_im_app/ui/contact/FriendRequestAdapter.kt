package com.scnu.blockchain_based_im_app.ui.contact

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R

class FriendRequestAdapter(val context: Context, val friendRequestList: List<Friend>) :
    RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>(){
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val applicantID : TextView = view.findViewById(R.id.applicantID)
            val applicantName : TextView = view.findViewById(R.id.applicantName)
            val applicantPhoto : ImageView = view.findViewById(R.id.applicantPhoto)
            val agree : Button = view.findViewById(R.id.agree)
            val refuse : Button = view.findViewById(R.id.refuse)
            val respond : TextView = view.findViewById(R.id.tv_respond)
        }

    @SuppressLint("Range")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.new_friend,parent,false)
        val viewHolder = ViewHolder(view)

        val dbHelper = MyDatabaseHelper(context, "IM_app.db", 2)
        val db: SQLiteDatabase = dbHelper.writableDatabase
        viewHolder.agree.setOnClickListener {
            val position = viewHolder.adapterPosition
            val applicantID = friendRequestList[position].id
            val applicantName = friendRequestList[position].name
            viewHolder.agree.visibility = Button.GONE
            viewHolder.refuse.visibility = Button.GONE
            viewHolder.respond.text = "已添加"
            viewHolder.respond.visibility = TextView.VISIBLE
            val cursor = db.rawQuery("select name from user where id=?", arrayOf(MainActivity.userID))
            if(cursor.moveToFirst()) {
                val respondentName = cursor.getString(cursor.getColumnIndex("name"))
                db.execSQL("insert into friendship(ownerID,friendID,note) values(?,?,?)", arrayOf(applicantID,MainActivity.userID,respondentName))
            }
            db.execSQL("insert into friendship(ownerID,friendID,note) values(?,?,?)", arrayOf(MainActivity.userID,applicantID,applicantName))
            db.execSQL("delete from friend_request where applicantID=? and respondentID=?", arrayOf(applicantID,MainActivity.userID))
        }

        viewHolder.refuse.setOnClickListener {
            val position = viewHolder.adapterPosition
            val applicantID = friendRequestList[position].id
            viewHolder.agree.visibility = Button.GONE
            viewHolder.refuse.visibility = Button.GONE
            viewHolder.respond.text = "已拒绝"
            viewHolder.respond.visibility = TextView.VISIBLE
            db.execSQL("delete from friend_request where applicantID=? and respondentID=?", arrayOf(applicantID,MainActivity.userID))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val applicant = friendRequestList[position]
        val applicantIDText = "id: ${applicant.id}"
        holder.applicantID.text = applicantIDText
        holder.applicantName.text = applicant.name
        holder.applicantPhoto.setImageBitmap(applicant.bitmap)
    }

    override fun getItemCount() = friendRequestList.size

}