package com.example.twowaits.network.dashboardApiCalls

import java.io.Serializable

data class FacultyProfileDetailsResponse(
    val department: String,
    val faculty_account_id: Int,
    val name: String,
    val profile_pic: String,
    val dob: String,
    val college: String,
    val gender: String,
    val contact_id: Int,
    val profile_pic_firebase: String
): Serializable