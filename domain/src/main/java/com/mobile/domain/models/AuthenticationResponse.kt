package com.mobile.domain.models

data class AuthenticationResponse(
    val message: String,
    val token: String,
    val user: User
)
