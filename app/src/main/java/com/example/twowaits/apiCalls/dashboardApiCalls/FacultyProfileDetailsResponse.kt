package com.example.twowaits.apiCalls.dashboardApiCalls

data class FacultyProfileDetailsResponse(
    val department: String,
    val faculty_account_id: Int,
    val name: String,
    val profile_pic: String,
    val dob: String,
    val college: String,
    val gender: String
)