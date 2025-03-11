package com.mobile.domain.usecases

import com.mobile.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthStatusUseCase @Inject constructor(
    private val repository: UserRepository
) {

    fun isLoggedIn(): Flow<Boolean> = repository.isLoggedIn

    suspend fun updateLoginState(isLoggedIn: Boolean) {
        repository.updateLoginState(isLoggedIn)
    }
}