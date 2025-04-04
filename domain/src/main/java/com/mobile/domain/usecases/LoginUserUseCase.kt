package com.mobile.domain.usecases

import com.mobile.domain.models.LoginRequest
import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

open class LoginUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(loginRequest: LoginRequest) = repository.loginUser(loginRequest)
}