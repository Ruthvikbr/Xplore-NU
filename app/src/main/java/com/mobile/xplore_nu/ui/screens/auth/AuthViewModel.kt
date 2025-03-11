package com.mobile.xplore_nu.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.LoginResponse
import com.mobile.domain.models.LogoutResponse
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.usecases.AuthStatusUseCase
import com.mobile.domain.usecases.LoginUserUseCase
import com.mobile.domain.usecases.LogoutUserUseCase
import com.mobile.domain.usecases.RegisterUserUseCase
import com.mobile.domain.utils.Resource
import com.mobile.xplore_nu.ui.uistates.LoginState
import com.mobile.xplore_nu.ui.uistates.LogoutState
import com.mobile.xplore_nu.ui.uistates.RegisterState
import com.mobile.xplore_nu.ui.utils.Validators.isValidEmail
import com.mobile.xplore_nu.ui.utils.Validators.isValidName
import com.mobile.xplore_nu.ui.utils.Validators.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    private val authStatusUseCase: AuthStatusUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _registerStatus =
        MutableStateFlow<Resource<UserRegisterResponse>>(Resource.loading(null))
    val registerStatus = _registerStatus.asStateFlow()

    private val _loginStatus = MutableStateFlow<Resource<LoginResponse>>(Resource.loading(null))
    val loginStatus = _loginStatus.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authStatusUseCase.isLoggedIn()
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {

        viewModelScope.launch {
            isLoggedIn.collect { value ->
                Log.d("MainViewModel", "Observed isLoggedIn: $value")
            }
        }

        registerState.distinctUntilChangedBy { it.firstName }.map {
            it.firstName.trim().isValidName()
        }
            .onEach { isFirstNameValid ->
                _registerState.update {
                    it.copy(
                        isFirstNameValid = isFirstNameValid
                    )
                }
            }.launchIn(viewModelScope)

        registerState.distinctUntilChangedBy { it.lastName }.map {
            it.lastName.trim().isValidName()
        }
            .onEach { isLastNameValid ->
                _registerState.update {
                    it.copy(
                        isLastNameValid = isLastNameValid
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
                            && state.isFirstNameValid && state.isLastNameValid && !state.isLoading && state.doPasswordsMatch
                )
            }
        }.launchIn(viewModelScope)

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

    fun updateEmail(email: String) {
        _registerState.update {
            it.copy(
                email = email
            )
        }
    }

    fun updateFirstName(firstName: String) {
        _registerState.update {
            it.copy(
                firstName = firstName
            )
        }
    }

    fun updateLastName(lastName: String) {
        _registerState.update {
            it.copy(
                lastName = lastName
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

    fun registerUser(state: RegisterState) {
        viewModelScope.launch(Dispatchers.IO) {
            _registerState.update {
                it.copy(
                    isLoading = true
                )
            }

            val response: Resource<UserRegisterResponse> = registerUserUseCase.invoke(
                UserRegisterBody(
                    state.firstName,
                    state.lastName,
                    state.email,
                    state.password
                )
            )
            //authStatusUseCase.updateLoginState(true)
            _registerStatus.emit(response)
            _registerState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
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

            val response: Resource<LoginResponse> = loginUserUseCase.invoke(
                LoginRequest(
                    state.email,
                    state.password
                )
            )
            //authStatusUseCase.updateLoginState(true)
            _loginStatus.emit(response)
            _loginState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }
}