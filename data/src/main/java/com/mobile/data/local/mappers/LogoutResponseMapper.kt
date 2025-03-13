package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DLogoutResponse
import com.mobile.domain.models.LogoutResponse

fun DLogoutResponse.toLogoutResponse() = LogoutResponse(message)

fun LogoutResponse.toDLogoutResponse() = DLogoutResponse(message)