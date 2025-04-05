package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DLeg
import com.mobile.domain.models.Leg

fun DLeg.toLeg() = Leg(viaWaypoints, admins.map { it.toAdmin() }, weight, duration, steps, distance, summary)

fun Leg.toDLeg() = DLeg(viaWaypoints, admins.map { it.toDAdmin() }, weight, duration, steps, distance, summary)