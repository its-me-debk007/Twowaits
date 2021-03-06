package com.example.twowaits.network.dashboardApiCalls.quizApiCalls

data class AddQuestionsResponse(
    val author_id: AuthorId,
    val description: String,
    val no_of_question: Int,
    val question: List<Question>,
    val quiz_id: Int,
    val time_limit: Int,
    val title: String
)