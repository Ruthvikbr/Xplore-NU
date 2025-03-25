package com.mobile.data.remote

import com.mobile.data.remote.models.DAuthenticationResponse
import com.mobile.data.remote.models.DLoginRequest
import com.mobile.data.remote.models.DLogoutResponse
import com.mobile.data.remote.models.DRequestOtpRequest
import com.mobile.data.remote.models.DRequestOtpResponse
import com.mobile.data.remote.models.DResendOtpRequest
import com.mobile.data.remote.models.DResendOtpResponse
import com.mobile.data.remote.models.DResetPasswordRequest
import com.mobile.data.remote.models.DResetPasswordResponse
import com.mobile.data.remote.models.DUpcomingEventsResponse
import com.mobile.data.remote.models.DUserRegisterBody
import com.mobile.data.remote.models.DUserResponse
import com.mobile.data.remote.models.DVerifyOtpRequest
import com.mobile.data.remote.models.DVerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {

    @POST("/auth/register")
    suspend fun registerUser(@Body requestBody: DUserRegisterBody): Response<DAuthenticationResponse>

    @POST("/auth/login")
    suspend fun loginUser(@Body dLoginRequest: DLoginRequest): Response<DAuthenticationResponse>

    @GET("/auth/Users")
    suspend fun getUsers(): Response<List<DUserResponse>>

    @POST("/auth/logout")
    suspend fun logoutUser(): Response<DLogoutResponse>

    @POST("/auth/forgot_password")
    suspend fun requestOtp(@Body dRequestOtpRequest: DRequestOtpRequest): Response<DRequestOtpResponse>

    @POST("auth/verify_otp")
    suspend fun verifyOtp(@Body dVerifyOtpRequest: DVerifyOtpRequest): Response<DVerifyOtpResponse>

    @POST("auth/resend_otp")
    suspend fun resendOtp(@Body dResendOtpRequest: DResendOtpRequest): Response<DResendOtpResponse>

    @POST("auth/reset_password")
    suspend fun resetPassword(@Body dResetPasswordRequest: DResetPasswordRequest): Response<DResetPasswordResponse>

    @GET("event/upcoming")
    suspend fun getUpcomingEvents(@Header("authorization") token: String): Response<DUpcomingEventsResponse>

}