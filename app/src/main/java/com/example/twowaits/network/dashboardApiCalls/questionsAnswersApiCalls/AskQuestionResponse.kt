package com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls

data class AskQuestionResponse(
    val author_id: Int,
    val id: Int,
    val question: String,
    val raised: String,
    val message: String
)