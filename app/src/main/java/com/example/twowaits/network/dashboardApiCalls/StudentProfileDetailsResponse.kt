package com.example.twowaits.network.dashboardApiCalls

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudentProfileDetailsResponse(
    val branch: String,
    val college: String,
    val course: String,
    val name: String,
    val profile_pic: String,
    val student_account_id: Int,
    var year: String,
    val detail: String? = null,
    val gender: String,
    val dob: String,
    var profile_pic_firebase: String
) : Parcelable