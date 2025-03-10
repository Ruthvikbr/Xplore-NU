package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DUserRegisterResponse
import com.mobile.domain.models.UserRegisterResponse

fun DUserRegisterResponse.toUserRegisterResponse() = UserRegisterResponse(message, token, user.toLoggedInUser())

fun UserRegisterResponse.toDUserRegisterResponse() = DUserRegisterResponse(message, token, user.toDLoggedInUser())