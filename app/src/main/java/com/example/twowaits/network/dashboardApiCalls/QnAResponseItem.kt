package com.example.twowaits.network.dashboardApiCalls

data class QnAResponseItem(
    val answer: List<Answer>,
    val author_id: AuthorIdXX,
    val question: String,
    val question_id: Int,
    val raised: String,
    val bookmarked_by_user: String
)