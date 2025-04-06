package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DAdmin
import com.mobile.domain.models.Admin

fun DAdmin.toAdmin() = Admin(iso31661Alpha3, iso31661)

fun Admin.toDAdmin() = DAdmin(iso31661Alpha3, iso31661)