package com.mobile.domain.usecases

import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class DataStoreUseCase @Inject constructor(
    private val repository: UserRepository
) {
    fun getIsLoggedIn() = repository.isLoggedIn

    fun getAuthToken() = repository.authToken

    suspend fun clearAuthToken() = repository.clearAuthToken()

    suspend fun clearIsLoggedIn() = repository.clearAuthToken()

    suspend fun setAuthToken(token: String, refreshToken: String) = repository.saveAuthTokens(token, refreshToken)

    suspend fun setIsLoggedIn(value: Boolean) = repository.setIsLoggedIn(value)
}