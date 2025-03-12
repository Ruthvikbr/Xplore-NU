package com.mobile.domain.models

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)
