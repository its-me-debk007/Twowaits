package com.example.twowaits.network.dashboardApiCalls.questionsAnswersApiCalls

data class LikeAnswerResponse(
    val answer_id: Int,
    val author_id: Int,
    val id: Int,
    val likes: Int
)