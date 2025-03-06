package com.mobile.domain.usecases

import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userRegisterBody: UserRegisterBody) = repository.registerUser(userRegisterBody)

    fun isLoggedIn() = repository.isLoggedIn
}