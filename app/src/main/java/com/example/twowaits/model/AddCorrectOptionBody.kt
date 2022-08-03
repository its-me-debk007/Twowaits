package com.example.twowaits.model

import com.example.twowaits.ui.fragment.quiz.CorrectOption

data class AddCorrectOptionBody(
    val question_id: Int,
    val correct_options: List<CorrectOption>
)