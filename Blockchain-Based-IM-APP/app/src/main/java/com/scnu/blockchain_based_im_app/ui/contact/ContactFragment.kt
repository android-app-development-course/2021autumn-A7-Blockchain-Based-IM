package com.scnu.blockchain_based_im_app.ui.contact

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scnu.blockchain_based_im_app.MainActivity
import com.scnu.blockchain_based_im_app.MyDatabaseHelper
import com.scnu.blockchain_based_im_app.R
import com.scnu.blockchain_based_im_app.databinding.FragmentContactBinding
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_contact.*
import java.io.ByteArrayOutputStream

class ContactFragment : Fragment() {

    private val friendList= ArrayList<Friend>()

    private lateinit var contactViewModel: ContactViewModel
    private var _binding: FragmentContactBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val userID = MainActivity.userID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactViewModel =
            ViewModelProvider(this).get(ContactViewModel::class.java)

        _binding = FragmentContactBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textContact
//        contactViewModel.text.observe(viewLifecycleOwner, Observer {
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

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.friendMenu -> {
                    showPopup(view)
                }
            }
        }
        friendMenu.setOnClickListener(clickListener)

        //下滑刷新
        //val swip_refresh_layout: SwipeRefreshLayout =findViewById(R.id.swipeLayout);
        swipeLayout0.setColorSchemeResources(R.color.green);
        swipeLayout0.setOnRefreshListener {
            Handler().postDelayed(
                Runnable { swipeLayout0.isRefreshing = false }, 2000
            )
        }

        //搜索
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0.toString().length > 0) {
                    val search_content=p0.toString()
                    var sum0=0
                    val contactsList00=ArrayList<Friend>()
                    val len= friendList.size
                    var i=0
                    while(i<len){
                        if(friendList[i].name.contains(search_content)||
                            friendList[i].id.contains(search_content)){
                            contactsList00.add(friendList[i])
                            sum0++
                        }
                        i++
                    }
                    showList(contactsList00)
                    Toast.makeText(requireContext(),"共搜索到"+sum0.toString()+"位好友",Toast.LENGTH_SHORT).show()

                }
                return false
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        search_view.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                //TODO("Not yet implemented")
                onResume()
                return false
            }
        })

        friendListView.setOnItemClickListener { _, _, position, _ ->
            val intent=Intent(requireActivity(), FriendActivity::class.java)//链接到好友主页
            intent.putExtra("friendID", friendList[position].id)
            startActivity(intent)
        }
    }

    private fun showList(array: ArrayList<Friend>){
        val adapter = FriendAdapter(requireContext(),R.layout.friends_item,  array)
        friendListView.adapter = adapter
    }

    @SuppressLint("Range")
    override fun onResume() {
        super.onResume()
        val adapter = FriendAdapter(requireContext(), R.layout.friends_item, friendList)
        friendListView.adapter = adapter

        friendList.clear()
        val dbHelper = MyDatabaseHelper(requireContext(), "IM_app.db", 2)
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor = db.rawQuery("select * from friendship where ownerID=$userID order by note",null)
        if(cursor.moveToFirst()) {
            do {
                val friendID = cursor.getString(cursor.getColumnIndex("friendID"))
                val friendNote = cursor.getString(cursor.getColumnIndex("note"))
                val cursor2 = db.rawQuery("select * from user where id=$friendID",null)
                if(cursor2.moveToFirst()) {
                    val friendPhotoByteChar: ByteArray = cursor2.getBlob(cursor2.getColumnIndex("profile_photo"))
                    val friendPhotoBitmap: Bitmap = BitmapFactory.decodeByteArray(friendPhotoByteChar,0,friendPhotoByteChar.size)
                    friendList.add(Friend(friendID, friendNote, friendPhotoBitmap))
                }
                cursor2.close()
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    private fun showPopup(view: View) {
        var popup: PopupMenu? = null;
        popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.friend)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.newFriendMenu -> {
                    val intent = Intent(requireActivity(), NewFriendActivity::class.java)//链接新的朋友
                    startActivity(intent)
                }
                R.id.addFriend -> {
                    val intent = Intent(requireActivity(),SearchFriendActivity::class.java)//链接添加朋友
                    startActivity(intent)
                }
            }

            true
        })

        popup.show()
    }

}