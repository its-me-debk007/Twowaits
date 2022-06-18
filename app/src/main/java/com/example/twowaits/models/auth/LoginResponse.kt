package com.example.twowaits.models.auth

data class LoginResponse(
    val message: String,
    val type: String,
    val contact_id: Int
)