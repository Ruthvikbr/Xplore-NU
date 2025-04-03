package com.mobile.xplore_nu.ui.screens.auth.register

import com.mobile.domain.models.AuthenticationResponse
import com.mobile.domain.models.User
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.repository.UserRepository
import com.mobile.domain.usecases.RegisterUserUseCase
import com.mobile.domain.utils.Resource
import com.mobile.domain.utils.Status
import com.mobile.xplore_nu.ui.screens.auth.register.RegisterViewModel
import com.mobile.xplore_nu.ui.uistates.RegisterState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class RegisterViewModelTest {

    private lateinit var registerViewModel: RegisterViewModel
    private val userRepository = mock<UserRepository>()
    private val registerUserUseCase = RegisterUserUseCase(userRepository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        registerViewModel = RegisterViewModel(registerUserUseCase)
    }

    @Test
    fun `registerUser success`() = runTest {
        // Given
        val firstName = "John"
        val lastName = "Doe"
        val email = "john.doe@example.com"
        val password = "Password123!"
        val registerState = RegisterState(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmPassword = password,
            isFirstNameValid = true,
            isLastNameValid = true,
            isEmailValid = true,
            isPasswordValid = true,
            isConfirmPasswordValid = true,
            doPasswordsMatch = true,
            canRegister = true
        )

        val expectedResponse = Resource.success(
            AuthenticationResponse(
                message = "Registration successful",
                token = "dummyToken",
                refreshToken = "dummyRefreshToken",
                user = User("1", firstName, lastName, email, "user")
            )
        )
        `when`(userRepository.registerUser(UserRegisterBody(firstName, lastName, email, password))).thenReturn(expectedResponse)

        // When
        registerViewModel.registerUser(registerState)

        // Then
        val actualStatus = registerViewModel.registerStatus.take(2).first {
            when (it.status) {
                Status.SUCCESS -> true
                Status.ERROR -> true
                Status.LOADING -> false
            }
        }
        assertEquals(expectedResponse.status, actualStatus.status)
        assertEquals(expectedResponse.data, actualStatus.data)
    }

    @Test
    fun `registerUser failure`() = runTest {
        // Given
        val firstName = "John"
        val lastName = "Doe"
        val email = "john.doe@example.com"
        val password = "password"
        val registerState = RegisterState(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            confirmPassword = password,
            isFirstNameValid = true,
            isLastNameValid = true,
            isEmailValid = true,
            isPasswordValid = true,
            isConfirmPasswordValid = true,
            doPasswordsMatch = true,
            canRegister = true
        )

        val expectedResponse = Resource.error<AuthenticationResponse>("Registration failed", null)
        `when`(userRepository.registerUser(UserRegisterBody(firstName, lastName, email, password))).thenReturn(expectedResponse)

        // When
        registerViewModel.registerUser(registerState)

        // Then
        val actualStatus = registerViewModel.registerStatus.take(2).first {
            when (it.status) {
                Status.SUCCESS -> true
                Status.ERROR -> true
                Status.LOADING -> false
            }
        }
        assertEquals(expectedResponse.status, actualStatus.status)
        assertEquals(expectedResponse.message, actualStatus.message)
    }
}