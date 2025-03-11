package com.mobile.domain.repository

import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.LoginResponse
import com.mobile.domain.models.LogoutResponse
import com.mobile.domain.models.User
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    suspend fun insertUser(user: User)

    fun getUser(id: String): User?

    suspend fun loginUser(loginRequest: LoginRequest): Resource<LoginResponse>

    suspend fun registerUser(userRegisterBody: UserRegisterBody): Resource<UserRegisterResponse>

    suspend fun logoutUser(): Resource<LogoutResponse>

    val isLoggedIn: Flow<Boolean>

    suspend fun clearAuthToken()

    val authToken: Flow<String?>

    suspend fun saveAuthToken(token: String)

    suspend fun updateLoginState(isLoggedIn: Boolean)

    fun observeAuthState(): Flow<Boolean>
}