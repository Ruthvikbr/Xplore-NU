package com.mobile.xplore_nu.ui.uistates

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isFullNameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val doPasswordsMatch: Boolean = false,
    val canRegister: Boolean = false,
    val isLoading: Boolean = false
)