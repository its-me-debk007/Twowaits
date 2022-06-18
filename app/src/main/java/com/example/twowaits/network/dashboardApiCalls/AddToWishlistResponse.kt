package com.example.twowaits.network.dashboardApiCalls

data class AddToWishlistResponse(
    val id: Int,
    val lecture_id: Int,
    val message: String,
    val user_id: Int
)