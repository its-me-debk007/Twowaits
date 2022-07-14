package com.example.twowaits.model.auth

data class GettingTokensResponse(
    val access: String,
    val refresh: String
)