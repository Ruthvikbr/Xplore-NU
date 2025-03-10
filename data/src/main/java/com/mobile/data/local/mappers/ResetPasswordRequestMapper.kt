package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DResetPasswordRequest
import com.mobile.data.remote.models.DResetPasswordResponse
import com.mobile.domain.models.ResetPasswordRequest
import com.mobile.domain.models.ResetPasswordResponse

fun ResetPasswordRequest.toDRequestPasswordRequest() = DResetPasswordRequest(email, newPassword)

fun DResetPasswordResponse.toResetPasswordResponse() = ResetPasswordResponse(message)
