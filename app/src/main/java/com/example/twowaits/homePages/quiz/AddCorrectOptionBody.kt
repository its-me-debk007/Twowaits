package com.example.twowaits.homePages.quiz

data class AddCorrectOptionBody(
    val question_id: Int,
    val correct_options: List<CorrectOption>
)