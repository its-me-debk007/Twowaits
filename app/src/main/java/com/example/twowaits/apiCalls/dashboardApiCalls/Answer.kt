package com.example.twowaits.apiCalls.dashboardApiCalls

data class Answer(
    val answer: String,
    val answer_id: Int,
    val answered: String,
    val author_id: AuthorId,
    val comment: List<Comment>,
    val likes: Int
)