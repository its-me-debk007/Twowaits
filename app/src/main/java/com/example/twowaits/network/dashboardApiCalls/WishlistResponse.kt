package com.example.twowaits.network.dashboardApiCalls

data class WishlistResponse(
    val author_id: Int,
    val description: String,
    val id: Int,
    val title: String,
    val uploaded: String,
    val video_firebase: String
)