package com.example.twowaits.authPages

data class VerifyOtpBody(
    val email: String,
    val otp: String
)