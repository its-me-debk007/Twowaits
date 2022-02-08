package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.chatRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.chatApiCalls.FetchConversationsMessagesResponse
import com.google.android.material.imageview.ShapeableImageView

class ChatProfilesRecyclerAdapter(
    private val chatProfiles: List<FetchConversationsMessagesResponse>,
    private val myContactId: Int,
    private val listener: ChatProfileClicked
) :
    RecyclerView.Adapter<ChatProfilesRecyclerAdapter.ChatProfilesViewHolder>() {
    inner class ChatProfilesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val profilePic: ShapeableImageView = itemView.findViewById(R.id.profilePic)
        val lastSeen: TextView = itemView.findViewById(R.id.lastSeen)
        init {
            itemView.setOnClickListener {
                listener.onChatProfileClicked()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatProfilesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_card_view, parent, false)
        return ChatProfilesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatProfilesViewHolder, position: Int) {
        holder.apply {
            val opponentPosition =
                if (chatProfiles[position].participants[0].contact_id == myContactId) 1 else 0
            profilePic.load(chatProfiles[position].participants[opponentPosition].contact_dp) {
                transformations(CircleCropTransformation())
            }
            name.text = chatProfiles[position].participants[opponentPosition].contact_name
        }
    }

    override fun getItemCount(): Int {
        return chatProfiles.size
    }
}
interface ChatProfileClicked {
    fun onChatProfileClicked()
}