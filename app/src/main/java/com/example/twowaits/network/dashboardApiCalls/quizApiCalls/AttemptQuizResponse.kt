package com.example.twowaits.network.dashboardApiCalls.quizApiCalls

data class AttemptQuizResponse(
    val quiz_id: Int,
    val quiz_result_id: Int,
    val student_id: Int,
    val message: String
)