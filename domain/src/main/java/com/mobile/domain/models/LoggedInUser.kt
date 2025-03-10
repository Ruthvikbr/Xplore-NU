package com.mobile.domain.models

data class LoggedInUser(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: String
)
