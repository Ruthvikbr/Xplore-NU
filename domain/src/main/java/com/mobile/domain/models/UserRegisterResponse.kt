package com.mobile.domain.models

data class UserRegisterResponse(
    val message: String,
    val token: String,
    val user: LoggedInUser
)
