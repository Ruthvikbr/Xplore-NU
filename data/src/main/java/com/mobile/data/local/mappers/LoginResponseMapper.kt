package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DLoginResponse
import com.mobile.domain.models.LoginResponse

fun DLoginResponse.toLoginResponse() = LoginResponse(message, token, dUser.toLoggedInUser())

fun LoginResponse.toDLoginResponse() = DLoginResponse(message, token, user.toDLoggedInUser())