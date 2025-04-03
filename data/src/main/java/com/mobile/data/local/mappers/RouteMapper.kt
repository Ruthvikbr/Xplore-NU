package com.mobile.data.local.mappers

import com.mobile.data.remote.models.DRoute
import com.mobile.domain.models.Route

fun DRoute.toRoute() = Route(weightName, weight, duration, distance, legs.map { it.toLeg() }, geometry)

fun Route.toDRoute() = DRoute(weightName, weight, duration, distance, legs.map { it.toDLeg() }, geometry)