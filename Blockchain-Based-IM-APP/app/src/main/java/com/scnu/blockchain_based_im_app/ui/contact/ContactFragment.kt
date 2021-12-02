package com.scnu.blockchain_based_im_app.ui.contact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.scnu.blockchain_based_im_app.R
import com.scnu.blockchain_based_im_app.databinding.FragmentContactBinding
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment() {

    private val friendList= ArrayList<Friend>()

    private lateinit var contactViewModel: ContactViewModel
    private var _binding: FragmentContactBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

        initFriend()
        val adapter = FriendAdapter(requireContext(), R.layout.friends_item, friendList)
        friendListView.adapter = adapter

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.friendMenu -> {
                    showPopup(view)
                }
            }
        }
        friendMenu.setOnClickListener(clickListener)

        friendListView.setOnItemClickListener { _, _, position, _ ->
            val friend=friendList[position]
            val intent=Intent(requireActivity(), FriendActivity::class.java)//链接到好友主页
            intent.putExtra("pos", position)
            startActivity(intent)
        }
    }

    private fun initFriend(){
        repeat(10){

            friendList.add(Friend("莫小叉", R.drawable.temp_profile_picture))
            friendList.add(Friend("吴", R.drawable.temp_profile_picture))
            friendList.add(Friend("子龙", R.drawable.temp_profile_picture))
        }
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
                    val intent = Intent(requireActivity(),AddFriendActivity::class.java)//链接添加朋友
                    startActivity(intent)
                }
            }

            true
        })

        popup.show()
    }

}