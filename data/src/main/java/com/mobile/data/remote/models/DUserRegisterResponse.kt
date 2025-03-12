package com.mobile.data.remote.models

data class DUserRegisterResponse(
    val message: String,
    val token: String,
    val user: DLoggedInUser
)
