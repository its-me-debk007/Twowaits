package com.example.twowaits.authPages

data class CreateStudentProfileBody(
    val name: String,
    val college: String,
    val course: String,
    val branch: String,
    val year: String,
    val gender: String,
    val dob: String,
    val profile_pic_firebase: String
)