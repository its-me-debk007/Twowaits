package com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls

data class QuestionXX(
    val correct: List<Correct>,
    val option: List<OptionXXX>,
    val question_text: String,
    val quiz_question_id: Int,
    val user_option: String
)