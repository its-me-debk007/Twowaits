package com.example.twowaits.network.dashboardApiCalls

data class Comment(
    val author_id: AuthorId,
    val comment: String,
    val comment_id: Int,
    val commented: String
)