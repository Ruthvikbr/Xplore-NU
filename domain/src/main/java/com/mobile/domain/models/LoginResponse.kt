package com.mobile.domain.models

data class LoginResponse(
    val message: String,
    val token: String,
    val user: LoggedInUser
)
