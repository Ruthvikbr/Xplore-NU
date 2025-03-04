package com.mobile.xplore_nu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.usecases.LoginUserUseCase
import com.mobile.domain.usecases.RegisterUserUseCase
import com.mobile.xplore_nu.ui.uistates.RegisterState
import com.mobile.xplore_nu.ui.utils.Validators.isValidEmail
import com.mobile.xplore_nu.ui.utils.Validators.isValidFullName
import com.mobile.xplore_nu.ui.utils.Validators.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState())
    val registerState = _registerState.asStateFlow()

    init {
        registerState.distinctUntilChangedBy { it.fullName }.map {
            it.fullName.trim().isValidFullName()
        }
            .onEach { isFullNameValid ->
                _registerState.update {
                    it.copy(
                        isFullNameValid = isFullNameValid
                    )
                }
            }.launchIn(viewModelScope)

        registerState.distinctUntilChangedBy { it.email }.map {
            it.email.trim().isValidEmail()
        }
            .onEach { isEmailValid ->
                _registerState.update {
                    it.copy(
                        isEmailValid = isEmailValid
                    )
                }
            }.launchIn(viewModelScope)

        registerState.distinctUntilChangedBy { it.password }.map {
            it.password.isValidPassword()
        }
            .onEach { isPasswordValid ->
                _registerState.update {
                    it.copy(
                        isPasswordValid = isPasswordValid
                    )
                }
            }.launchIn(viewModelScope)

        registerState.distinctUntilChangedBy { it.confirmPassword }
            .map { it.confirmPassword.isValidPassword() }.onEach { isConfirmPasswordValid ->
                _registerState.update {
                    it.copy(
                        isConfirmPasswordValid = isConfirmPasswordValid
                    )
                }
            }.launchIn(viewModelScope)

        combine(
            registerState.map { it.password },
            registerState.map { it.confirmPassword }
        ) { password, confirmPassword ->
            when {
                confirmPassword.length <= 1 -> true  // No error when confirm password is empty
                else -> password == confirmPassword
            }
        }.distinctUntilChanged()
            .onEach { doPasswordsMatch ->
                _registerState.update { it.copy(doPasswordsMatch = doPasswordsMatch) }
            }.launchIn(viewModelScope)

        registerState.onEach { state ->
            _registerState.update {
                it.copy(
                    canRegister = state.isEmailValid && state.isPasswordValid && state.isConfirmPasswordValid
                            && state.isFullNameValid && !state.isLoading && state.doPasswordsMatch
                )
            }
        }.launchIn(viewModelScope)
    }

    fun updateEmail(email: String) {
        _registerState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updateFullName(fullName: String) {
        _registerState.update {
            it.copy(
                fullName = fullName
            )
        }
    }

    fun updatePassword(password: String) {
        _registerState.update {
            it.copy(
                password = password
            )
        }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _registerState.update {
            it.copy(
                confirmPassword = confirmPassword
            )
        }
    }

    fun registerUser() {

    }
}