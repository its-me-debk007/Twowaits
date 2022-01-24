package com.example.twowaits.apiCalls.dashboardApiCalls

data class StudentProfileDetailsResponse(
    val branch: String,
    val college: String,
    val course: String,
    val name: String,
    val profile_pic: String,
    val student_account_id: Int,
    var year: String,
    val detail: String,
    val gender: String,
    val dob: String,
    val profile_pic_firebase: String
)