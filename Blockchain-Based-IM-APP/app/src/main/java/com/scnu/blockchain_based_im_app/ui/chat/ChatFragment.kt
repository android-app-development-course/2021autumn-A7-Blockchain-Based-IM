package com.scnu.blockchain_based_im_app.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.scnu.blockchain_based_im_app.R
import com.scnu.blockchain_based_im_app.databinding.FragmentChatBinding
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {

    private lateinit var chatViewModel: ChatViewModel
    private var _binding: FragmentChatBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


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
        val layoutManager = LinearLayoutManager(requireActivity())
        chatsRecycleView.layoutManager = layoutManager
        val adapter = ChatAdapter(requireContext(), getChats())
        chatsRecycleView.adapter = adapter
    }

    private fun getChats(): List<Chat> {
        val chatsList = ArrayList<Chat>()
        for(i in 1..10) {
            val chat = Chat(R.drawable.temp_profile_picture, "好友$i", "和好友${i}的最后一条消息")
            chatsList.add(chat)
        }
        return chatsList
    }

}