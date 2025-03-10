package com.mobile.xplore_nu.ui.uistates

data class OtpState(
    val otp: String = "",
    val isOtpFilled: Boolean = false,
    val isValidOtp: Boolean = true,
    val canVerifyOtp: Boolean = false,
    val isLoading: Boolean = false
)