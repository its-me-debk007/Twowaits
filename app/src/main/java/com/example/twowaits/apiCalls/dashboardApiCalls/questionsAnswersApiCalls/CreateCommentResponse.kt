package com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls

data class CreateCommentResponse(
    val answer_id: Int,
    val author_id: Int,
    val comment: String,
    val commented: String,
    val id: Int
)