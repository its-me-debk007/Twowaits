package com.example.twowaits.network.dashboardApiCalls

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FacultyProfileDetailsResponse(
    val department: String,
    val faculty_account_id: Int,
    val name: String,
    val profile_pic: String,
    val dob: String,
    val college: String,
    val gender: String,
    var profile_pic_firebase: String? = null
) : Parcelable