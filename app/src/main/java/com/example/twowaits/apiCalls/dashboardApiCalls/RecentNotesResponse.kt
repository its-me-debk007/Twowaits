package com.example.twowaits.apiCalls.dashboardApiCalls

data class RecentNotesResponse(
    val author_id: Int,
    val description: String,
    val id: Int,
    val note_file: List<Any>,
    val title: String,
    val uploaded: String
)