package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DUserRegisterBody
import com.mobile.domain.models.UserRegisterBody

fun UserRegisterBody.toDUserRegisterBody() = DUserRegisterBody(firstName, lastName, email, password)