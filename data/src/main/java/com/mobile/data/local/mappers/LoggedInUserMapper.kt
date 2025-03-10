package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DLoggedInUser
import com.mobile.domain.models.LoggedInUser

fun DLoggedInUser.toLoggedInUser() = LoggedInUser(id, firstName, lastName, email, role)

fun LoggedInUser.toDLoggedInUser() = DLoggedInUser(id, firstName, lastName, email, role)
