package com.example.twowaits.apiCalls.dashboardApiCalls

data class UploadLectureResponse(
    val author_id: Int,
    val description: String,
    val id: Int,
    val title: String,
    val uploaded: String,
    val video_firebase: String
)