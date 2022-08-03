package com.example.twowaits.model

data class CreateQuizBody(
    val title: String,
    val description: String?,
    val no_of_question: Int,
    val time_limit: Int
)