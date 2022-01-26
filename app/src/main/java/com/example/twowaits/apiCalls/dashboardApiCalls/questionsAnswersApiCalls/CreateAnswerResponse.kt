package com.example.twowaits.apiCalls.dashboardApiCalls.questionsAnswersApiCalls

data class CreateAnswerResponse(
    val answer: String,
    val answered: String,
    val author_id: Int,
    val id: Int,
    val question_id: Int
)