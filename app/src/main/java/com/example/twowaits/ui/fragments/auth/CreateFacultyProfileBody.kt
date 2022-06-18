package com.example.twowaits.ui.fragments.auth

data class CreateFacultyProfileBody(
    val college: String,
    val department: String,
    val dob: String,
    val gender: String,
    val name: String,
    val profile_pic_firebase: String
)