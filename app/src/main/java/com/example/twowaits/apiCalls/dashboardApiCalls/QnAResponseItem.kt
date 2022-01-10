package com.example.twowaits.apiCalls.dashboardApiCalls

data class QnAResponseItem(
    val answer: List<Answer>,
    val author_id: AuthorIdXX,
    val question: String,
    val question_id: Int,
    val raised: String
)