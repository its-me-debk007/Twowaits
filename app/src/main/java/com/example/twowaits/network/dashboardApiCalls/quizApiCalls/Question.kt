package com.example.twowaits.network.dashboardApiCalls.quizApiCalls

data class Question(
    val correct: List<Any>,
    val option: List<Option>,
    val question_text: String,
    val quiz_question_id: Int
)