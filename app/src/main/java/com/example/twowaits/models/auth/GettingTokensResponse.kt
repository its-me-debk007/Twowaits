package com.example.twowaits.models.auth

data class GettingTokensResponse(
    val access: String,
    val refresh: String
)