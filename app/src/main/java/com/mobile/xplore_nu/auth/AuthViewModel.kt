package com.mobile.xplore_nu.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.mobile.domain.usecases.LoginUserUseCase
import com.mobile.domain.usecases.LogoutUserUseCase
import com.mobile.domain.usecases.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase
): ViewModel() {

    var email = mutableStateOf("").value
        private set

    var password = mutableStateOf("").value
        private set

    fun setEmail(inputString: String) {
        email = inputString
    }

    fun setPassword(inputString: String) {
        password = inputString
    }
}