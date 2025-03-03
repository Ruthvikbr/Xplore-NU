package com.mobile.data.remote.models

data class DLoginResponse(
    val message: String,
    val token: String,
    val dLoggedInUser: DLoggedInUser
)
