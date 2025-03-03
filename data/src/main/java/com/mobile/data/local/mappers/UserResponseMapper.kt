package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DUserResponse
import com.mobile.domain.models.UserResponse

fun DUserResponse.toUserResponse() = UserResponse(id, firstName, lastName, email, role)

fun UserResponse.toDUserResponse() = DUserResponse(id, firstName, lastName, email, role)
