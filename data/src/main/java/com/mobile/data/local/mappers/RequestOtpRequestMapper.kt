package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DRequestOtpRequest
import com.mobile.data.remote.models.DRequestOtpResponse
import com.mobile.domain.models.RequestOtpRequest
import com.mobile.domain.models.RequestOtpResponse

fun RequestOtpRequest.toDRequestOtpRequest() = DRequestOtpRequest(email)

fun DRequestOtpResponse.toRequestOtpResponse() = RequestOtpResponse(message)
