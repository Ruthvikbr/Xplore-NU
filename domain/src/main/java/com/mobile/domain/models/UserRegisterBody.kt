package com.mobile.domain.models

data class UserRegisterBody (
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)