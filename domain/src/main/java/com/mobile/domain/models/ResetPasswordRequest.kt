package com.mobile.domain.models

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)
