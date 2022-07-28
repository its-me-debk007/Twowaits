package com.example.twowaits.model.auth

data class TokenResponse(
    val access: String,
    val refresh: String? = null
)