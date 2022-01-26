package com.example.twowaits.apiCalls.authApiCalls

data class LoginResponse(
    val message: String,
    val type: String,
    val contact_id: Int
)