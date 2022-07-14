package com.example.twowaits.model.auth

data class SignUpResponse(
    val email: String,
    val password: String,
    val message: String
)