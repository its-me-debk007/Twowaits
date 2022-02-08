package com.example.twowaits.apiCalls.dashboardApiCalls

data class UploadNotesResponse(
    val author_id: Int,
    val description: String,
    val file_obj_firebase: String,
    val id: Int,
    val title: String,
    val uploaded: String
)