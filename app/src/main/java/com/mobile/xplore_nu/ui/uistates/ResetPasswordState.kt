package com.mobile.xplore_nu.ui.uistates

data class ResetPasswordState(
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val doPasswordsMatch: Boolean = false,
    val isResetButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)