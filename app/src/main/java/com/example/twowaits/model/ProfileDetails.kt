package com.example.twowaits.model

import java.io.Serializable

data class ProfileDetails(
    val student_account_id: Int? = null,
    val faculty_account_id: Int? = null,
    val name: String,
    val college: String,
    val department: String? = null,
    val course: String? = null,
    val branch: String? = null,
    var year: String? = null,
    val gender: String,
    val dob: String,
    val profile_pic: String,
    val profile_pic_firebase: String? = null,
    val contact_id: Int? = null,
): Serializable