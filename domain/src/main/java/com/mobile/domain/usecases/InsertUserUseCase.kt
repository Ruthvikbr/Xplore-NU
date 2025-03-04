package com.mobile.domain.usecases

import com.mobile.domain.models.User
import com.mobile.domain.repository.UserRepository

class InsertUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User) = repository.insertUser(user)
}