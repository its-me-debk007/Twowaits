package com.example.twowaits.apiCalls.dashboardApiCalls

data class RecentQuizzesResponse(
    val author_id: AuthorIdXXX,
    val description: String,
    val no_of_question: Int,
    val question: List<Any>,
    val quiz_id: Int,
    val time_limit: Int,
    val title: String
)