package com.example.twowaits.models.auth

data class SignUpResponse(
    val email: String,
    val password: String,
    val message: String
)