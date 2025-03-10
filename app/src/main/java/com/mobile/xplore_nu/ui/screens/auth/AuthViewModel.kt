package com.mobile.xplore_nu.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.domain.models.RequestOtpResponse
import com.mobile.domain.models.ResendOtpResponse
import com.mobile.domain.models.ResetPasswordResponse
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.models.VerifyOtpResponse
import com.mobile.domain.usecases.LoginUserUseCase
import com.mobile.domain.usecases.RegisterUserUseCase
import com.mobile.domain.usecases.ResetPasswordUseCase
import com.mobile.domain.utils.Resource
import com.mobile.xplore_nu.ui.uistates.ForgotPasswordState
import com.mobile.xplore_nu.ui.uistates.OtpState
import com.mobile.xplore_nu.ui.uistates.RegisterState
import com.mobile.xplore_nu.ui.uistates.ResetPasswordState
import com.mobile.xplore_nu.ui.utils.Validators.isValidEmail
import com.mobile.xplore_nu.ui.utils.Validators.isValidName
import com.mobile.xplore_nu.ui.utils.Validators.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.distinctUntilChangedBy
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
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState())
    val registerState = _registerState.asStateFlow()

    private val _registerStatus =
        MutableStateFlow<Resource<UserRegisterResponse>>(Resource.loading(null))
    val registerStatus = _registerStatus.asStateFlow()

    val isLoggedIn: StateFlow<Boolean?> = registerUserUseCase.isLoggedIn()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _forgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState())
    val forgotPasswordState = _forgotPasswordState.asStateFlow()

    private val _requestOtpStatus =
        MutableStateFlow<Resource<RequestOtpResponse>>(Resource.loading(null))
    val requestOtpStatus = _requestOtpStatus.asStateFlow()

    private val _otpState = MutableStateFlow<OtpState>(OtpState())
    val otpState = _otpState.asStateFlow()

    private val _verifyOtpStatus =
        MutableStateFlow<Resource<VerifyOtpResponse>>(Resource.loading(null))
    val verifyOtpStatus = _verifyOtpStatus.asStateFlow()

    private val _resendOtpStatus =
        MutableStateFlow<Resource<ResendOtpResponse>>(Resource.loading(null))
    val resendOtpStatus = _resendOtpStatus.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState())
    val resetPasswordState = _resetPasswordState.asStateFlow()

    private val _resetPasswordStatus =
        MutableStateFlow<Resource<ResetPasswordResponse>>(Resource.loading(null))
    val resetPasswordStatus = _resetPasswordStatus.asStateFlow()

    init {

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


        forgotPasswordState.distinctUntilChangedBy { it.email }.map {
            it.email.trim().isValidEmail()
        }
            .onEach { isEmailValid ->
                _forgotPasswordState.update {
                    it.copy(
                        isEmailValid = isEmailValid
                    )
                }
            }.launchIn(viewModelScope)

        forgotPasswordState.onEach { state ->
            _forgotPasswordState.update {
                it.copy(
                    canRequestOtp = state.isEmailValid && !state.isLoading
                )
            }
        }.launchIn(viewModelScope)

        otpState.distinctUntilChangedBy { it.otp }.map {
            it.otp.length == 6
        }
            .onEach { isOtpFilled ->
                _otpState.update {
                    it.copy(
                        isOtpFilled = isOtpFilled
                    )
                }
            }.launchIn(viewModelScope)

        otpState.onEach { state ->
            _otpState.update {
                it.copy(
                    canVerifyOtp = state.isOtpFilled
                )
            }
        }.launchIn(viewModelScope)

        resetPasswordState.distinctUntilChangedBy { it.password }.map {
            it.password.isValidPassword()
        }
            .onEach { isPasswordValid ->
                _resetPasswordState.update {
                    it.copy(
                        isPasswordValid = isPasswordValid
                    )
                }
            }.launchIn(viewModelScope)

        resetPasswordState.distinctUntilChangedBy { it.confirmPassword }.map {
            it.confirmPassword.isValidPassword()
        }
            .onEach { isConfirmPasswordValid ->
                _resetPasswordState.update {
                    it.copy(
                        isConfirmPasswordValid = isConfirmPasswordValid
                    )
                }
            }.launchIn(viewModelScope)


        combine(
            resetPasswordState.map { it.password },
            resetPasswordState.map { it.confirmPassword }
        ) { password, confirmPassword ->
            when {
                confirmPassword.length <= 1 -> true  // No error when confirm password is empty
                else -> password == confirmPassword
            }
        }.distinctUntilChanged()
            .onEach { doPasswordsMatch ->
                _resetPasswordState.update { it.copy(doPasswordsMatch = doPasswordsMatch) }
            }.launchIn(viewModelScope)

        resetPasswordState.onEach { state ->
            _resetPasswordState.update {
                it.copy(
                    isResetButtonEnabled = state.isPasswordValid && state.isConfirmPasswordValid && state.doPasswordsMatch
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
            _registerStatus.emit(response)
            _registerState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun forgotPasswordEmailUpdated(email: String) {
        _forgotPasswordState.update {
            it.copy(
                email = email
            )
        }
    }

    fun requestOtp(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _forgotPasswordState.update {
                it.copy(
                    isLoading = true
                )
            }
            val response: Resource<RequestOtpResponse> =
                resetPasswordUseCase.requestOtp(email)
            _requestOtpStatus.emit(response)

            _forgotPasswordState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _otpState.update {
                it.copy(
                    isLoading = true
                )
            }
            val response: Resource<VerifyOtpResponse> = resetPasswordUseCase.verifyOtp(email, otp)
            _verifyOtpStatus.emit(response)

            _otpState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun updateOtp(otp: String) {
        _otpState.update {
            it.copy(
                otp = otp
            )
        }
    }

    fun resendOtp(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _otpState.update {
                it.copy(
                    isLoading = true
                )
            }
            val response: Resource<ResendOtpResponse> = resetPasswordUseCase.resendOtp(email)
            _resendOtpStatus.emit(response)

            _otpState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun resetRequestOtpStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            _requestOtpStatus.emit(Resource.loading(null))
        }
    }

    fun resetResendOtpStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            _resendOtpStatus.emit(Resource.loading(null))
        }
    }

    fun resetVerifyOtpStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            _verifyOtpStatus.emit(Resource.loading(null))
        }
    }

    fun updateNewPassword(password: String) {
        _resetPasswordState.update {
            it.copy(password = password)
        }
    }

    fun updateNewConfirmPassword(password: String) {
        _resetPasswordState.update {
            it.copy(confirmPassword = password)
        }
    }

    fun resetPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _resetPasswordState.update {
                it.copy(
                    isLoading = true
                )
            }
            val response: Resource<ResetPasswordResponse> = resetPasswordUseCase.resetPassword(email, password)
            _resetPasswordStatus.emit(response)

            _resetPasswordState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }


}