package com.example.twowaits.homePages.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.databinding.ChatProfileBinding
import com.example.twowaits.viewModel.ChatViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class ChatProfile : Fragment() {
    private var _binding: ChatProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatProfileBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}