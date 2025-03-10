package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DResendOtpRequest
import com.mobile.data.remote.models.DResendOtpResponse
import com.mobile.domain.models.ResendOtpRequest
import com.mobile.domain.models.ResendOtpResponse

fun ResendOtpRequest.toDResendOtpRequest() = DResendOtpRequest(email)

fun DResendOtpResponse.toResendOtpResponse() = ResendOtpResponse(message)
