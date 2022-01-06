package com.example.twowaits.apiCalls.authApiCalls

data class GettingTokensResponse(
    val access: String,
    val refresh: String
)