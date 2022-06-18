package com.example.twowaits.ui.fragments.auth

data class VerifyOtpBody(
    val email: String,
    val otp: String
)