package com.mobile.domain.repository

import com.mobile.domain.models.RequestOtpRequest
import com.mobile.domain.models.RequestOtpResponse
import com.mobile.domain.models.LoginRequest
import com.mobile.domain.models.LoginResponse
import com.mobile.domain.models.ResendOtpRequest
import com.mobile.domain.models.ResendOtpResponse
import com.mobile.domain.models.ResetPasswordRequest
import com.mobile.domain.models.ResetPasswordResponse
import com.mobile.domain.models.User
import com.mobile.domain.models.UserRegisterBody
import com.mobile.domain.models.UserRegisterResponse
import com.mobile.domain.models.VerifyOtpRequest
import com.mobile.domain.models.VerifyOtpResponse
import com.mobile.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insertUser(user: User)

    fun getUser(id: String): User?

    suspend fun loginUser(loginRequest: LoginRequest): LoginResponse

    suspend fun registerUser(userRegisterBody: UserRegisterBody): Resource<UserRegisterResponse>

    suspend fun logoutUser(): UserRegisterResponse

    suspend fun setIsLoggedIn(isLoggedIn: Boolean)

    val isLoggedIn: Flow<Boolean>

    suspend fun clearAuthToken()

    val authToken: Flow<String?>

    suspend fun saveAuthToken(token: String)

    suspend fun requestOtp(requestOtpRequest: RequestOtpRequest): Resource<RequestOtpResponse>

    suspend fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): Resource<VerifyOtpResponse>

    suspend fun resendOtp(resendOtpRequest: ResendOtpRequest): Resource<ResendOtpResponse>

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Resource<ResetPasswordResponse>

}