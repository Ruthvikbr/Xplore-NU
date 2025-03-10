package com.mobile.domain.usecases

import com.mobile.domain.models.RequestOtpRequest
import com.mobile.domain.models.ResendOtpRequest
import com.mobile.domain.models.ResetPasswordRequest
import com.mobile.domain.models.VerifyOtpRequest
import com.mobile.domain.repository.UserRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun requestOtp(email: String) = repository.requestOtp(RequestOtpRequest(email))

    suspend fun verifyOtp(email: String, otp: String) =
        repository.verifyOtp(VerifyOtpRequest(email, otp))

    suspend fun resendOtp(email: String) =
        repository.resendOtp(ResendOtpRequest(email))

    suspend fun resetPassword(email: String, newPassword: String) =
        repository.resetPassword(ResetPasswordRequest(email, newPassword))
}