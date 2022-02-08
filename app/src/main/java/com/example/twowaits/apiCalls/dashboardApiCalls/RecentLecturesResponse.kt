package com.example.twowaits.apiCalls.dashboardApiCalls

data class RecentLecturesResponse(
    val author_id: AuthorIdX,
    val description: String,
    val id: Int,
    val title: String,
    val uploaded: String,
    val video_firebase: String,
    val wishlisted_by_user: String
)