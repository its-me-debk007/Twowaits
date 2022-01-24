package com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls

data class BookmarkQuestionResponse(
    val id: Int,
    val message: String,
    val question_id: Int,
    val user_id: Int
)