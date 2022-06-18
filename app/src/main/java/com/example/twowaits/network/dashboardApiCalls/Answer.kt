package com.example.twowaits.network.dashboardApiCalls

data class Answer(
    val answer: String,
    val answer_id: Int,
    val answered: String,
    val author_id: AuthorId,
    val comment: List<Comment>,
    val likes: Int,
    val liked_by_user: String
)