package com.example.twowaits.network.dashboardApiCalls.quizApiCalls

data class RegisterResponseBody(
    val quiz_result_id: Int,
    val question_id: Int,
    val options: List<OptionXX>
)