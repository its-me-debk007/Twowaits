package com.example.twowaits.homePages.quiz

data class CreateQuestionBody(
    val quiz_id: Int,
    val question_text: String,
    val options: List<Option>
)