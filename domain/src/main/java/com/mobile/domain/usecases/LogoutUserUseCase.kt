package com.mobile.domain.usecases

import com.mobile.domain.repository.UserRepository

class LogoutUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke() = repository.logoutUser()
}