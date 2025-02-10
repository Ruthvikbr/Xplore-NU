package com.nbapps.data.remote

import com.nbapps.domain.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @GET("/users/{username}")
    suspend fun getUser(@Path("username") username: String)

    @POST("/api/users")
    suspend fun createUser(@Body user: User): User
}