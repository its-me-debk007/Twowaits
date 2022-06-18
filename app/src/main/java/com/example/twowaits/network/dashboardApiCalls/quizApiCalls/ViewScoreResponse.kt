package com.example.twowaits.network.dashboardApiCalls.quizApiCalls

data class ViewScoreResponse(
    val attempted: Int,
    val correct: Int,
    val quiz_result_score_id: Int,
    val title: String,
    val total_questions: Int,
    val total_score: String,
    val wrong: Int
)