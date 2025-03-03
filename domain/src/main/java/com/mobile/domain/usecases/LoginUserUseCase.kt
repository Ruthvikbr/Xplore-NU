package com.mobile.domain.usecases

import com.mobile.domain.models.LoginRequest
import com.mobile.domain.repository.UserRepository

class LoginUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest) = repository.loginUser(loginRequest)
}