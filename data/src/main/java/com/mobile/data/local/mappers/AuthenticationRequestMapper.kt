package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DLoginRequest
import com.mobile.domain.models.LoginRequest

fun LoginRequest.toDLoginRequest() = DLoginRequest(email, password)
