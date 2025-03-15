package com.mobile.data.remote.models

data class DResetPasswordRequest(
    val email: String,
    val newPassword: String
)
