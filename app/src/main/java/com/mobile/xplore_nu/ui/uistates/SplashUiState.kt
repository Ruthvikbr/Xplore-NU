package com.mobile.xplore_nu.ui.uistates

sealed class SplashUiState {
    object Authenticated: SplashUiState()
    data class Splash(
        val isLoading: Boolean = false,
        val moveToLogin: Boolean = false
    ): SplashUiState()
}