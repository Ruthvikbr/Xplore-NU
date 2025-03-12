package com.mobile.xplore_nu.ui.uistates

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isFirstNameValid: Boolean = false,
    val isLastNameValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val doPasswordsMatch: Boolean = true,
    val canRegister: Boolean = false,
    val isLoading: Boolean = false
)