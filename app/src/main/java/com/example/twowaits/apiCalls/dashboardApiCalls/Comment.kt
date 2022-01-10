package com.example.twowaits.apiCalls.dashboardApiCalls

data class Comment(
    val author_id: AuthorIdX,
    val comment: String,
    val comment_id: Int,
    val commented: String
)