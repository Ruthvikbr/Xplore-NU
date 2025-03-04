package com.mobile.data.remote

import com.mobile.data.remote.models.DLoginRequest
import com.mobile.data.remote.models.DLoginResponse
import com.mobile.data.remote.models.DUserResponse
import com.mobile.data.remote.models.DUserRegisterBody
import com.mobile.data.remote.models.DUserRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {

    @POST("/auth/register")
    suspend fun registerUser(@Body requestBody: DUserRegisterBody): Response<DUserRegisterResponse>

    @POST("/auth/login")
    suspend fun loginUser(@Body DLoginRequest: DLoginRequest): Response<DLoginResponse>

    @GET("/auth/Users")
    suspend fun getUsers(): Response<List<DUserResponse>>

    @POST("/auth/logout")
    suspend fun logoutUser(): Response<DUserRegisterResponse>

}