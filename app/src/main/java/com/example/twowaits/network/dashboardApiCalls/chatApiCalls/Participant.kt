package com.example.twowaits.network.dashboardApiCalls.chatApiCalls

data class Participant(
    val contact_dp: String,
    val contact_id: Int,
    val contact_name: String,
    val status: Boolean
)