package com.scnu.blockchain_based_im_app.ui.chat

import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scnu.blockchain_based_im_app.R
import com.scnu.blockchain_based_im_app.databinding.FragmentChatBinding
import kotlinx.android.synthetic.main.fragment_chat.*

import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.ui.contact.Friend
import kotlinx.android.synthetic.main.fragment_contact.*
import java.io.ByteArrayOutputStream

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val chatList= ArrayList<Chat>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        chatViewModel =
            ViewModelProvider(this).get(ChatViewModel::class.java)

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textMessage
//        chatViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //下滑刷新
        //val swip_refresh_layout: SwipeRefreshLayout =findViewById(R.id.swipeLayout);
        swipeLayout01.setColorSchemeResources(R.color.green);
        swipeLayout01.setOnRefreshListener {
            Handler().postDelayed(
                Runnable { swipeLayout01.isRefreshing = false }, 2000
            )
        }
    }

    override fun onResume() {
        super.onResume()
        val layoutManager = LinearLayoutManager(requireActivity())
        chatsRecycleView.layoutManager = layoutManager
        initChatList()
        val adapter = ChatAdapter(requireActivity(), requireContext(), chatList)
        chatsRecycleView.adapter = adapter
    }

    private fun initChatList(){
        chatList.clear()
        val dbHelper = MyDatabaseHelper(requireContext(), "IM_app.db", 2)
        val db = dbHelper.readableDatabase
        //把置顶的聊天项加到chatList中
        var cursor = db.rawQuery("select * from chat where ownerID=? and stickOnTop = 1 order by lastTime DESC",
            arrayOf(MainActivity.userID))
        addChat(cursor)
        cursor.close()
        //把非置顶的聊天项加到chatList中
        cursor = db.rawQuery("select * from chat where ownerID=? and stickOnTop = 0 order by lastTime DESC",
            arrayOf(MainActivity.userID))
        addChat(cursor)
        cursor.close()
    }

    @SuppressLint("Range")
    private fun addChat(cursor: Cursor) {
        val dbHelper = MyDatabaseHelper(requireContext(), "IM_app.db", 2)
        val db = dbHelper.readableDatabase
        if(cursor.moveToFirst()) {
            do {
                val friendID = cursor.getString(cursor.getColumnIndex("friendID"))
                val lastMessage = cursor.getString(cursor.getColumnIndex("lastMessage"))
                val lastTime = cursor.getString(cursor.getColumnIndex("lastTime"))
                val stickOnTop = cursor.getInt(cursor.getColumnIndex("stickOnTop"))
                val msgIgnore = cursor.getInt(cursor.getColumnIndex("msgIgnore"))

                val cursor2 = db.rawQuery("select * from user where id=?", arrayOf(friendID))
                val cursor3 = db.rawQuery("select * from friendship where ownerID=? and friendID=?",
                    arrayOf(MainActivity.userID,friendID))
                if(cursor2.moveToFirst() && cursor3.moveToFirst()) {
                    val photoByteChar:ByteArray = cursor2.getBlob(cursor2.getColumnIndex("profile_photo"))
                    val bitmap: Bitmap = BitmapFactory.decodeByteArray(photoByteChar, 0, photoByteChar.size)
                    val chatterName = cursor3.getString(cursor3.getColumnIndex("note"))
                    chatList.add(Chat(friendID, bitmap, chatterName, lastMessage, lastTime, stickOnTop, msgIgnore))
                }
                cursor2.close()
                cursor3.close()
            } while (cursor.moveToNext())
        }
    }

}