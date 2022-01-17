package com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls

data class CreateQuizResponse(
    val author_id: Int,
    val description: String,
    val no_of_question: Int,
    val quiz_id: Int,
    val time_limit: Int,
    val title: String,
    val detail: String
)