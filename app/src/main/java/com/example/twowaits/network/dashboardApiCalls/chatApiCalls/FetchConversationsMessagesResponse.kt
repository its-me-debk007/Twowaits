package com.example.twowaits.network.dashboardApiCalls.chatApiCalls

data class FetchConversationsMessagesResponse(
    val conv_message: List<ConvMessage>,
    val conversation_id: Int,
    val name: String,
    val participants: List<Participant>
)