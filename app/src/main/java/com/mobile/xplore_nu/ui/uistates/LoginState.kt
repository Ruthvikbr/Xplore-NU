package com.mobile.xplore_nu.ui.uistates

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isLoading: Boolean = false,
    val canLogin: Boolean = false
)
