package com.example.twowaits.network.dashboardApiCalls.quizApiCalls

data class QuestionX(
    val option: List<OptionX>,
    val question_text: String,
    val quiz_question_id: Int
)