package com.mobile.xplore_nu.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.models.AuthenticationResponse
import com.mobile.domain.models.LoginRequest
import com.mobile.domain.usecases.LoginUserUseCase
import com.mobile.domain.utils.Resource
import com.mobile.xplore_nu.ui.uistates.LoginState
import com.mobile.xplore_nu.ui.utils.Validators.isValidEmail
import com.mobile.xplore_nu.ui.utils.Validators.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _loginStatus =
        MutableStateFlow<Resource<AuthenticationResponse>>(Resource.loading(null))
    val loginStatus = _loginStatus.asStateFlow()

    init {
        loginState.onEach { state ->
            _loginState.update {
                it.copy(
                    canLogin = state.isEmailValid && state.isPasswordValid && !state.isLoading
                )
            }
        }.launchIn(viewModelScope)

        loginState.distinctUntilChangedBy { it.email }.map {
            it.email.trim().isValidEmail()
        }
            .onEach { isValidEmail ->
                _loginState.update {
                    it.copy(
                        isEmailValid = isValidEmail
                    )
                }
            }.launchIn(viewModelScope)

        loginState.distinctUntilChangedBy { it.password }.map {
            it.password.trim().isValidPassword()
        }
            .onEach { isValidPassword ->
                _loginState.update {
                    it.copy(
                        isPasswordValid = isValidPassword
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun updateEmailForLogin(email: String) {
        _loginState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updatePasswordForLogin(password: String) {
        _loginState.update {
            it.copy(
                password = password
            )
        }
    }

    fun loginUser(state: LoginState) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginState.update {
                it.copy(
                    isLoading = true
                )
            }

            val response: Resource<AuthenticationResponse> = loginUserUseCase.invoke(
                LoginRequest(
                    state.email,
                    state.password
                )
            )
            _loginStatus.emit(response)
            _loginState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }
}