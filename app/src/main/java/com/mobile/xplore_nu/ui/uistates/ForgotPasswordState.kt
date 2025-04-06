package com.mobile.xplore_nu.ui.uistates

data class ForgotPasswordState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    val canRequestOtp: Boolean = false,
    val isLoading: Boolean = false
)