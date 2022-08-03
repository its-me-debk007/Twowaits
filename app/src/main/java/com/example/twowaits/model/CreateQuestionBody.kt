package com.example.twowaits.model

data class CreateQuestionBody(
    val quiz_id: Int,
    val question_text: String,
    val options: List<Option>
)