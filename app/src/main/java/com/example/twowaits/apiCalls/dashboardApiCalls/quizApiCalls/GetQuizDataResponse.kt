package com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls

data class GetQuizDataResponse(
    val author_id: AuthorIdX,
    val description: String,
    val no_of_question: Int,
    val question: List<QuestionX>,
    val quiz_id: Int,
    val time_limit: Int,
    val title: String
)