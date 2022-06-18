package com.example.twowaits.network.dashboardApiCalls

data class BookmarkNoteResponse(
    val id: Int,
    val message: String,
    val note_id: Int,
    val user_id: Int
)