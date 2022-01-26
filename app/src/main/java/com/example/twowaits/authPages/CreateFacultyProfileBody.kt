package com.example.twowaits.authPages

data class CreateFacultyProfileBody(
    val name: String,
    val department: String,
    val college: String,
    val gender: String,
    val dob: String,
    val profile_pic_firebase: String
)