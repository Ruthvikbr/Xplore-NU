package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DLoggedInUser
import com.mobile.domain.models.LoggedInUser

fun DLoggedInUser.toLoggedInUser() = LoggedInUser(id, email)

fun LoggedInUser.toDLoggedInUser() = DLoggedInUser(id, email)
