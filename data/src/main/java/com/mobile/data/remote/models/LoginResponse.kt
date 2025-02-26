package com.mobile.data.remote.models

data class LoginResponse(
    val message: String,
    val token: String,
    val loggedInUser: LoggedInUser
)
