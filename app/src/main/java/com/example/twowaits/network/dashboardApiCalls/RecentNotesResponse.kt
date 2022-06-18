package com.example.twowaits.network.dashboardApiCalls

data class RecentNotesResponse(
    val author_id: AuthorIdX,
    val description: String,
    val id: Int,
    val note_file: List<Any>,
    val title: String,
    val uploaded: String,
    val bookmarked_by_user: String,
    val file_obj_firebase: String
)