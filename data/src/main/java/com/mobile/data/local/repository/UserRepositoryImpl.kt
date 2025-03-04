package com.mobile.data.local.repository

import com.mobile.data.local.mappers.toDLoginRequest
import com.mobile.data.local.mappers.toDUser
import com.mobile.data.local.mappers.toDUserRegisterBody
import com.mobile.data.local.mappers.toLoginResponse
import com.mobile.data.local.mappers.toUser
import com.mobile.data.local.mappers.toUserRegisterResponse
import com.mobile.data.local.models.DUser
import com.mobile.data.local.room.Dao
import com.mobile.data.remote.UserService
import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.LoginResponse
import com.mobile.domain.models.User
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val userService: UserService
) : UserRepository {
    override suspend fun insertUser(user: User) {
        dao.insertUser(user.toDUser())
    }

    override fun getUser(id: String): User? {
        return dao.getUser(id)?.toUser()
    }

    override suspend fun loginUser(loginRequest: LoginRequest): LoginResponse {
        val response = userService.loginUser(loginRequest.toDLoginRequest())
        return response.body()?.toLoginResponse() ?: throw Exception("Login failed")
    }

    override suspend fun registerUser(userRegisterBody: UserRegisterBody): UserRegisterResponse {
        val response = userService.registerUser(userRegisterBody.toDUserRegisterBody())
        return response.body()?.toUserRegisterResponse() ?: throw Exception("Registration failed")
    }

    override suspend fun logoutUser(): UserRegisterResponse {
        val response = userService.logoutUser()
        return response.body()?.toUserRegisterResponse() ?: throw Exception("Logout failed")
    }
}