package com.mobile.domain.usecases

import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.repository.UserRepository

class RegisterUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userRegisterBody: UserRegisterBody) = repository.registerUser(userRegisterBody)
}