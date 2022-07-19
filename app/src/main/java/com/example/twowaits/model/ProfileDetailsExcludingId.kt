package com.example.twowaits.model

import java.io.Serializable

data class ProfileDetailsExcludingId(
    val name: String,
    val college: String,
    val department: String? = null,
    val course: String? = null,
    val branch: String? = null,
    var year: String? = null,
    val gender: String,
    val dob: String,
    val profile_pic_firebase: String? = null,
): Serializable