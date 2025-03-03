package com.mobile.domain.repository

import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.LoginResponse
import com.mobile.domain.models.User
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse

interface UserRepository {

    suspend fun insertUser(user: User)

    fun getUser(id: String): User?

    suspend fun loginUser(loginRequest: LoginRequest): LoginResponse

    suspend fun registerUser(userRegisterBody: UserRegisterBody): UserRegisterResponse

    suspend fun logoutUser(): UserRegisterResponse
}