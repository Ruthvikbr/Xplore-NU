package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DLoginResponse
import com.mobile.domain.models.LoginResponse

fun DLoginResponse.toLoginResponse() = LoginResponse(message, token, dLoggedInUser.toLoggedInUser())

fun LoginResponse.toDLoginResponse() = DLoginResponse(message, token, loggedInUser.toDLoggedInUser())