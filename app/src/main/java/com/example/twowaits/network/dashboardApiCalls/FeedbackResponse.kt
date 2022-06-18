package com.example.twowaits.network.dashboardApiCalls

data class FeedbackResponse(
    val author_id: Int,
    val id: Int,
    val message: String,
    val rating: Int,
    val recorded: String
)