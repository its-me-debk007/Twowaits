package com.example.twowaits.homePages.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.databinding.AskBinding
import com.example.twowaits.databinding.ChatBinding
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.chatRecyclerAdapters.ChatProfileClicked
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.chatRecyclerAdapters.ChatProfilesRecyclerAdapter
import com.example.twowaits.viewmodels.ChatViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class ChatList: Fragment(), ChatProfileClicked {
    private var _binding: ChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        viewModel.fetchConversationsMessages()
        viewModel.fetchLiveData.observe(viewLifecycleOwner, {
            binding.chatProfilesRecyclerView.adapter = ChatProfilesRecyclerAdapter(it, 1, this)
            binding.chatProfilesRecyclerView.layoutManager = object: LinearLayoutManager(context){
                override fun canScrollVertically(): Boolean = false
            }
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navGraph = findNavController().graph
                navGraph.startDestination = R.id.chatList
                findNavController().graph = navGraph
                findNavController().navigate(R.id.action_chatList_to_homePage)
                val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView?.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onChatProfileClicked() {
        findNavController().navigate(R.id.action_chatList_to_chatProfile)
    }
}