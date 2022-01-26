package com.example.twowaits.homePages

data class UploadNoteBody(
    val description: String,
    val title: String,
    val file_obj_firebase: String
)