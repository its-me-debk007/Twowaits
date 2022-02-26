package com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls

data class DetailedQuizResultResponse(
    val author_id: AuthorIdXX,
    val description: String,
    val no_of_question: Int,
    val question: List<QuestionXX>,
    val quiz_id: Int,
    val time_limit: Int,
    val title: String
)