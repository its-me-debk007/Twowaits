package com.example.twowaits.homePages.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.MessageListener
import com.example.twowaits.WebSocketManager
import com.example.twowaits.databinding.ChatBinding
import com.example.twowaits.databinding.ChatProfileBinding
import com.example.twowaits.viewmodels.ChatViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlin.concurrent.thread

@DelicateCoroutinesApi
class ChatProfile: Fragment(), MessageListener {
    private var _binding: ChatProfileBinding? = null
    private val binding get() = _binding!!

    private val serverUrl = "ws://3.110.33.189/ws/chat/1/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatProfileBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        WebSocketManager.init(serverUrl, this)
        binding.connectBtn.setOnClickListener {
            thread {
                kotlin.run {
                    WebSocketManager.connect()
                }
            }
        }
        binding.clientSendBtn.setOnClickListener {
            if ( WebSocketManager .sendMessage( "Client send" )) {
                addText( " Send from the client \n " )
            }
        }
        binding.closeConnectionBtn.setOnClickListener {
            WebSocketManager.close()
        }

        return binding.root
    }

    override fun onConnectSuccess() {
        addText( " Connected successfully \n " )
    }

    override fun onConnectFailed() {
        addText( " Connection failed \n " )
    }

    override fun onClose() {
        addText( " Closed successfully \n " )
    }

    override fun onMessage(text: String?) {
        addText( " Receive message: $text \n " )
    }

    private fun addText(text: String?) {
        binding.contentEt.text.append(text)
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.close()
        _binding = null
    }
}