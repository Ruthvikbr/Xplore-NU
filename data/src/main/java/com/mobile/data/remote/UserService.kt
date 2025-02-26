package com.mobile.data.remote

import com.mobile.data.remote.models.LoginRequest
import com.mobile.data.remote.models.LoginResponse
import com.mobile.data.remote.models.User
import com.mobile.data.remote.models.UserRegisterBody
import com.mobile.data.remote.models.UserRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("/auth/register")
    suspend fun registerUser(@Body requestBody: UserRegisterBody): Response<UserRegisterResponse>

    @POST("/auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("/auth/Users")
    suspend fun getUsers(): Response<List<User>>

    @POST("/auth/logout")
    suspend fun logoutUser(): Response<UserRegisterResponse>

}