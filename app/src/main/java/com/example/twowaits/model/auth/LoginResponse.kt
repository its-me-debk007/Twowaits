package com.example.twowaits.model.auth

data class LoginResponse(
    val message: String,
    val type: String,
    val contact_id: Int
)