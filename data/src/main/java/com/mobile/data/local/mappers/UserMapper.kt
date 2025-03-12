package com.mobile.data.local.mappers

import com.mobile.data.local.models.DUser
import com.mobile.domain.models.User

fun DUser.toUser() = User(id, firstName, lastName, email, role)

fun User.toDUser() = DUser(id, firstName, lastName, email, role)

