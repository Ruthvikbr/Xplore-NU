package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DVerifyOtpRequest
import com.mobile.data.remote.models.DVerifyOtpResponse
import com.mobile.domain.models.VerifyOtpRequest
import com.mobile.domain.models.VerifyOtpResponse

fun VerifyOtpRequest.toDVerifyOtpRequest() = DVerifyOtpRequest(email, otp)

fun DVerifyOtpResponse.toVerifyOtpResponse() = VerifyOtpResponse(message)
