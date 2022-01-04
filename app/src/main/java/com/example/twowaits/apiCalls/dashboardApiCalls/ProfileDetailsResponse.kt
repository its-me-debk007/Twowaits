package com.example.twowaits.apiCalls.dashboardApiCalls

data class ProfileDetailsResponse(
    val branch: String,
    val college: String,
    val course: String,
    val interest: String,
    val name: String,
    val profile_pic: String,
    val student_account_id: Int,
    val year: String,
    val detail: String
)