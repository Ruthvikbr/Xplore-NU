package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DUserRegisterResponse
import com.mobile.domain.models.UserRegisterResponse

fun DUserRegisterResponse.toUserRegisterResponse() = UserRegisterResponse(message, token)

fun UserRegisterResponse.toDUserRegisterResponse() = DUserRegisterResponse(message, token)